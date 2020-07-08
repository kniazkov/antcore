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
package com.kniazkov.antcore.runtime.web;

import com.kniazkov.json.JsonBoolean;
import com.kniazkov.json.JsonElement;
import com.kniazkov.json.JsonParser;
import com.kniazkov.webserver.FormData;
import com.kniazkov.webserver.Response;
import com.kniazkov.webserver.ResponseJson;

import java.util.Map;
import java.util.TreeMap;

/**
 * Common handler for HTTP requests
 */
public class Handler implements com.kniazkov.webserver.Handler {
    public Handler(WebExecutor executor) {
        respondents = new TreeMap<>();
        respondents.put("hello", new Hello(executor));
        respondents.put("create instance", new CreateInstance(executor));
        respondents.put("update", new Update(executor));
    }

    @Override
    public Response handle(Map<String, FormData> request)
    {
        if (request.containsKey("request")) {
            Respondent respondent = respondents.get(request.get("request").getValue());
            if (respondent != null) {
                JsonElement data = null;
                if(request.containsKey("data")) {
                    data = JsonParser.parse(request.get("data").getValue());
                }
                return respondent.respond(data);
            }
        }
        System.err.println("Unknown request: '" + request.get("request").getValue() + "'");
        return new ResponseJson(new JsonBoolean(null, false));
    }

    @Override
    public Response handle(String address) {
        return null;
    }

    private Map<String, Respondent> respondents;
}
