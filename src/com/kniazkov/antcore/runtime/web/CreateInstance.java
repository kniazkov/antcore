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

import com.kniazkov.antcore.basic.bytecode.CompiledModule;
import com.kniazkov.json.JsonElement;
import com.kniazkov.json.JsonObject;
import com.kniazkov.webserver.Response;
import com.kniazkov.webserver.ResponseJson;

/**
 * The 'create instance' handler
 */
public class CreateInstance extends Respondent {
    public CreateInstance(WebExecutor executor) {
        super(executor);
    }

    @Override
    public Response respond(JsonElement data) {
        JsonObject obj = data.toJsonObject();
        if (obj == null)
            return null;

        JsonElement pageElem = obj.get("page");
        if (pageElem == null || !pageElem.isString())
            return null;

        String page = pageElem.stringValue();
        CompiledModule module = executor.getModuleByName(page);
        Ant ant = new Ant(executor.getTicks(), executor, module.getBytecode());
        String uid = ant.getUId();
        executor.ants.put(uid, ant);
        System.out.println("The ant '" + uid + "' was born, population: " + executor.ants.size());
        JsonObject result = new JsonObject(null);
        result.createString("uid", uid);
        return new ResponseJson(result);
    }
}
