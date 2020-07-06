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
package com.kniazkov.json;

public class JsonParser {
	static public JsonElement parse(String str) {
		return parse(new Source(str), null);
	}

	static protected class Source {
		Source(String data) {
			this.data = data;
			idx = 0;
			maxIdx = data != null ? data.length() : 0;
		}
		
		private String data;
		private int idx;
		private int maxIdx;
		
		public char get() {
			if (idx < maxIdx)
				return data.charAt(idx);
			else
				return 0;
		}
		
		public char getButNotSpace() {
			char c = get();
			while (isSpace(c))
				c = next();
			return c;
		}

		public char next() {
			if (idx < maxIdx)
			{
				idx++;
				return get();
			}
			else
				return 0;
		}
		
		public char nextButNotSpace() {
			char c = next();
			while (isSpace(c))
				c = next();
			return c;
		}

		static protected boolean isSpace(char c) {
			switch(c) {
			case ' ':
			case '\n':
			case '\r':
			case '\t':
				return true;
			default:
				return false;
			}
		}
	}
	
	static protected JsonElement parse(Source src, JsonElement parent) {
		char c = src.getButNotSpace();
		
		switch(c) {
			case 0:
				return null;
			case '{':
				src.next();
				return parseObject(src, parent);
			case '[':
				src.next();
				return parseArray(src, parent);
			case '"': {
				src.next();
				String value = parseString(src);
				return value != null ? new JsonString(parent, value) : null;
			}
			case '-':
				c = src.next();
				if (isDigit(c))
					return parseNumber(src, parent, true);
				break;
		}

		if (isDigit(c))
			return parseNumber(src, parent, false);

		if (isLetter(c)) {
			StringBuilder b = new StringBuilder();
			do {
				b.append(c);
				c = src.next();
			} while(isLetter(c));
			switch(b.toString()) {
			case "true":
				return new JsonBoolean(parent, true);
			case "false":
				return new JsonBoolean(parent, false);
			case "null":
				return new JsonNull(parent);
			}
		}

		return null;
	}
	
	static protected JsonObject parseObject(Source src, JsonElement parent) {
		JsonObject obj = new JsonObject(parent);
		int count = 0;
		
		while(true) {
			char c = src.getButNotSpace();
						
			if (c == 0)
				return null;
			if (c == '}') {
				src.next();
				return obj;
			}
			if (count > 0) {
				if (c != ',')
					return null;
				c = src.nextButNotSpace();
				if (c == 0)
					return null;
			}
			String name = null;
			if (c == '\"') {
				src.next();
				name = parseString(src);
			}
			else if (isLetter(c))
			{
				StringBuilder b = new StringBuilder();
				do
				{
					b.append(c);
					c = src.next();
				} while(isLetter(c) || isDigit(c));
				name = b.toString();
			}
			if (name == null)
				return null;
			c = src.getButNotSpace();
			if (c != ':')
				return null;
			c = src.nextButNotSpace();
			if (c == 0)
				return null;
			JsonElement elem = parse(src, obj);
			if (elem == null)
				return null;
			obj.addElement(name, elem);
			count++;
		}
	}

	static protected JsonArray parseArray(Source src, JsonElement parent) {
		JsonArray arr = new JsonArray(parent);
		int count = 0;
		
		while(true) {
			char c = src.getButNotSpace();
						
			if (c == 0)
				return null;
			if (c == ']') {
				src.next();
				return arr;
			}
			if (count > 0) {
				if (c != ',')
					return null;
				c = src.nextButNotSpace();
				if (c == 0)
					return null;
			}
			JsonElement elem = parse(src, arr);
			if (elem == null)
				return null;
			arr.addElement(elem);
			count++;
		}
	}
	
	static protected String parseString(Source src) {
		StringBuilder b = new StringBuilder();
		char c = src.get();
		while (c != '\"' && c != 0) {
			if (c == '\\') {
				c = src.next();
				switch(c) {
				case '"':
					b.append('"');
					break;
				case '\\':
					b.append('\\');
					break;
				case '/':
					b.append('/');
					break;
				case 'b':
					b.append('\b');
					break;
				case 'f':
					b.append('\f');
					break;
				case 'n':
					b.append('\n');
					break;
				case 'r':
					b.append('\r');
					break;
				case 't':
					b.append('\t');
					break;
				case 'u': {
					int k = 0;
					for (int i = 0; i < 4; i++)
					{
						c = src.next();
						if (!isHexDigit(c))
							return null;
						k = (k << 4) | convertHexDigit(c);
					}
					c = (char)k;
					b.append(c);
					break;					
				}
				default:
					return null;
				}
			}
			else
				b.append(c);
			c = src.next();
		}
		if (c == 0)
			return null;
		src.next();
		return b.toString();
	}
	
	static protected JsonNumber parseNumber(Source src, JsonElement parent, boolean neg) {
		StringBuilder b = new StringBuilder();
		char c = src.get();
		do {
			b.append(c);
			c = src.next();
		} while(isDigit(c));
		if (c == '.') {
			b.append(c);
			c = src.next();
			if (isDigit(c)) {
				do
				{
					b.append(c);
					c = src.next();
				} while(isDigit(c));
			}
		}
		try {
			double value = Double.parseDouble(b.toString());
			return new JsonNumber(parent, neg ? -value : value);
		}
		catch (NumberFormatException e) {
			return null;
		}
	}
	
	static protected boolean isLetter(char c) {
		return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_';
	}
	
	static protected boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}

	static protected boolean isHexDigit(char c) {
		return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
	}
	
	static protected int convertHexDigit(char c) {
		if (c >= '0' && c <= '9')
			return c - '0';
		if (c >= 'A' && c <= 'F')
			return c - 'A' + 10;
		if (c >= 'a' && c <= 'f')
			return c - 'a' + 10;
		return -1;
	}
}
