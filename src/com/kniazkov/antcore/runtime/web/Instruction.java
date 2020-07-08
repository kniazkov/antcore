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

import java.util.UUID;

/**
 * Instruction to be sent to the web page.
 * Execution of the instruction causes the displayed data to change.
 */
public abstract class Instruction {
    public Instruction() {
        uid = UUID.randomUUID().toString();
    }

    public String getUId() {
        return uid;
    }

    /**
     * @return type of instruction
     */
    public abstract String getType();

    /**
     * Fills a JSON object with additional parameters
     * @param obj destination object
     */
    protected abstract void fillJsonObject(JsonObject obj);

    /**
     * Converts an instruction to JSON object
     * @param obj destination object
     */
    public void toJsonObject(JsonObject obj) {
        obj.createString("uid", getUId());
        obj.createString("type", getType());
        fillJsonObject(obj);
    }

    private String uid;
}
