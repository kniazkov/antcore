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

import com.kniazkov.json.JsonObject;
import com.kniazkov.json.JsonString;

/**
 * The input field
 */
public class Input extends Widget {
    public Input(int id) {
        super(id);
    }

    @Override
    public String getType() {
        return "input";
    }

    @Override
    boolean setData(String data) {
        value = data;
        return true;
    }

    @Override
    String getData() {
        return value;
    }

    @Override
    void handleEvent(String type, JsonObject event) {
        if (type.equals("input")) {
            if (event.containsKey("value")) {
                value = event.get("value").stringValue();
            }
        }
    }

    String value;
}
