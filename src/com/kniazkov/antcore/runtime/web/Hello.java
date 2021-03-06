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

import com.kniazkov.json.JsonElement;
import com.kniazkov.webserver.Response;
import com.kniazkov.webserver.ResponseText;

/**
 * 'Hello' respondent for testing purposes
 */
public class Hello extends Respondent {
    public Hello(WebExecutor executor) {
        super(executor);
    }

    @Override
    public Response respond(JsonElement data) {
        return new ResponseText("hi.");
    }
}
