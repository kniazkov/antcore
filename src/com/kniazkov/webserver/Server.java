/*
 * Copyright (C) 2020 Ivan Kniazkov
 *
 * This file is part of Antcore.
 *
 * Antcore is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Antcore is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Antcore.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.kniazkov.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Server {
	public static Server start(Options opt, Handler handler) {
		Server server = new Server(opt, handler);
		server.start();
		return server;
	}
	
	private Server(Options opt, Handler handler) {
		listener = new Listener(opt, handler);
		thread = new Thread(listener);
	}
	
	private void start() {
		thread.start();
	}
	
	public boolean isAlive() {
		return thread.isAlive();
	}
	
	public void stop() {
		listener.stop();
	}
	
	private Listener listener;
	private Thread thread;
	
	private static class Listener implements Runnable {
		private Options opt;
		private Handler handler;
		private volatile boolean work;
	
		public Listener(Options opt, Handler handler) {
			this.opt = opt;
			this.handler = handler;
			work = true;
		}

		public void run() {
			ServerSocket ss = null;
			try {
		        ss = new ServerSocket(opt.port);
		        ExecutorService pool = Executors.newFixedThreadPool(opt.threadCount);
		        while (work)
		        {
		            Socket s = ss.accept();
		            pool.submit(new Processor(s, opt.wwwRoot, handler));
		        }
		        pool.shutdownNow();
			}
			catch (Throwable e) {
				System.err.println("HTTP server failed: " + e.toString());
			}

			try {
				if (ss != null)
					ss.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void stop() {
			work = false;
		}
	}
	
    private static class Processor implements Runnable {
    	private enum Method {
    		UNKNOWN,
    		GET,
    		POST
    	}

        private Processor(Socket socket, String wwwRoot, Handler handler) {
        	this.socket = socket;
        	this.wwwRoot = wwwRoot;
        	this.handler = handler;
        	this.method = Method.UNKNOWN;
        }

        private Socket socket;
        private String wwwRoot;
        private Handler handler;
        private String address;
        private Method method;
        private int contentLength;
        private String postData;
        private String boundary;

        public void run() {
            try {
            	writeResponse("102 Processing", null, null);
            	
                BufferedReader buff = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while(true)
                {
                    String s = buff.readLine();
                    if(s == null || s.trim().length() == 0) {
                        break;
                    }
                    if (s.startsWith("GET")) {
                    	int n = s.indexOf("HTTP");
                    	if (n != -1)
                    		address = s.substring(4,  n - 1);
                    	method = Method.GET;
                    }
                    else if (s.startsWith("POST")) {
                    	int n = s.indexOf("HTTP");
                    	if (n != -1)
                    		address = s.substring(5,  n - 1);
                    	method = Method.POST; 
                    }
                    else if (s.startsWith("Content-Length:")) {
                    	try {
                    		contentLength = Integer.parseInt(s.substring(16));
                    	}
                    	catch(NumberFormatException e) {
                    		contentLength = 0;
                    	}
                    }
                    else if (s.startsWith("Content-Type: multipart/form-data;")) {
                    	int n = s.indexOf("boundary=");
                    	if (n != -1)
                    		boundary = s.substring(n + 9);
                    }
                }
                if (method == Method.POST && contentLength > 0) {
                	Thread.sleep(250);
                	char[] tmp = new char[contentLength];
                	char[] data = new char[contentLength];
                	int offset = 0;
                	int readCnt;
                	while (offset < contentLength) {
                		readCnt = buff.read(tmp);
                		if (readCnt == -1)
                			break;
                		System.arraycopy(tmp, 0, data, offset, readCnt);
                		offset += readCnt;
                    	writeResponse("202 Accepted", null, null);
                	}
            		postData = String.valueOf(data);
                }
                
                if (method == Method.UNKNOWN) {
            		writeResponse("200 OK", "text/javascript", null);
                }
                else if ((address != null && address.startsWith("/?")) || (method == Method.POST && boundary == null)) {
                	TreeMap<String, FormData> map = new TreeMap<String, FormData>();
                	String request;
                	if (method == Method.GET)
                		request = address.substring(2);
                	else
                		request = postData;
                	String[] split = request.split("&");
                	for(String pair : split) {
                		if (pair != null && !pair.equals("")) {
                    		String[] keyVal = pair.split("=");
                    		if (keyVal.length == 2) {
                    			String key = URLDecoder.decode(keyVal[0], "UTF-8");
                    			FormData value = new FormData(URLDecoder.decode(keyVal[1], "UTF-8"));
                    			map.put(key, value);
                    		}
                		}
                	}
                	Response response = handler.handle(map);
                	if (response != null)
						writeResponse("200 OK", response.getContentType(), response.getData());
                	else
                		writeResponse("500 Internal Server Error", null, null);
                }
                else if (method == Method.POST && boundary != null) {
                	TreeMap<String, FormData> map = new TreeMap<String, FormData>();
                	String[] split = postData.split("--" + boundary);
                	for(String part : split) {
                		int i = part.indexOf("Content-Disposition");
                		if (i > -1 && i < 10) {
                			int j = part.indexOf("\r\n\r\n");
                			if (j > -1) {
            					String fileName = null;
                				String value = part.substring(j + 4, part.length() - 2);
                				String header = part.substring(0, j);
                				int k = header.indexOf("name=\"");
                				if (k > -1)
                				{
                					header = header.substring(k + 6);
                					k = header.indexOf('"');
                					String key = header.substring(0, k);
                					header = header.substring(k + 1);
                					k = header.indexOf("filename=\"");
                					if (k > -1)
                					{
                						header = header.substring(k + 10);
                						k = header.indexOf('"');
                						fileName = header.substring(0, k);
                					}
                					map.put(key, new FormData(fileName, value));
                				}
                			}
                		}
                	}
                	Response response = handler.handle(map);
                	if (response != null)
                		writeResponse("200 OK", response.getContentType(), response.getData());
                	else
                		writeResponse("500 Internal Server Error", null, null);
                }
                else {
                	int paramsIdx = address.indexOf('?');
                	if (paramsIdx >= 0)
                		address = address.substring(0, paramsIdx);
                	if (address.equals("/") || address == null)
                		address = "/index.html";
                	else
                		address = URLDecoder.decode(address, "UTF-8");
                	Response response = handler.handle(address);
                	if (response != null) {
                		writeResponse("200 OK", response.getContentType(), response.getData());
                	}
                	else {
	                	String path = wwwRoot + address;
	                	try {
	                		File file = new File(path);
	                		if (file.exists())
	                		{
	                			byte[] data = Files.readAllBytes(file.toPath());
	                			String ext = null;
	                			int i = address.lastIndexOf('.');
	                			if (i > 0)
	                				ext = address.substring(i + 1).toLowerCase();
	                			String type = "application/unknown";
	                			if (ext != null) {
	                				switch(ext)
	                				{
	                				case "htm":
	                				case "html":
	                					type = "text/html";
	                					break;
	                				case "css":
	                					type = "text/css";
	                					break;
	                				case "js":
	                					type = "text/javascript";
	                					break;
	                				case "jpg":
	                				case "jpeg":
	                				case "png":
	                				case "gif":
	                					type = "image/" + ext;
	                					break;
	                				default:
	                					type = "application/" + ext;
	                				}
	                			}
	                    		writeResponse("200 OK", type, data);
	                		}
	                		else
	                    		writeResponse("404 Not Found", null, null);
	                	}
	                	catch (Throwable t) {
	                		writeResponse("500 Internal Server Error", null, null);
	                	}
                	}
                }
            }
            catch (Throwable t) {
            	if (!(t instanceof java.net.SocketException))
            		t.printStackTrace();
            }
            finally {
                try {
                    socket.close();
                }
                catch (Throwable t) {
                	t.printStackTrace();
                }
            }
        }

        private void writeResponse(String code, String type, byte[] data) throws Throwable {
        	OutputStream stream = socket.getOutputStream();
        	if (code != null) {
	        	if (type == null)
	        		type = "application/unknown";
	        	StringBuilder b = new StringBuilder();

	        	b.append("HTTP/1.1 ");
	        	b.append(code);
	        	b.append("\r\n");
	            
	        	b.append("Access-Control-Allow-Origin: *\r\n");
	        	
	        	b.append("Content-Type: ");
	        	b.append(type);
	        	b.append("\r\n");
	        	
	        	b.append("Content-Length: ");
	        	if (data != null)
	        		b.append(data.length);
	        	else
	        		b.append('0');
	        	b.append("\r\n");

	        	b.append("Connection: close\r\n");

	        	b.append("\r\n");
	        	
	            stream.write(b.toString().getBytes());
        	}
            if (data != null)
            	stream.write(data);
            stream.flush();
        }
    }
}
