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

import com.kniazkov.json.*;
import com.kniazkov.webserver.Response;
import com.kniazkov.webserver.ResponseJson;

import java.util.*;

/**
 * The 'update' handler
 */
public class Update extends Respondent {
    public Update(WebExecutor executor) {
        super(executor);
    }

    @Override
    public Response respond(JsonElement data) {
        int k, count;

        JsonObject obj = data.toJsonObject();
        if (obj == null)
            return null;

        JsonElement uidElem = obj.get("uid");
        if (uidElem == null || !uidElem.isString())
            return null;

        JsonElement transactionElem = obj.get("transaction");
        if (transactionElem == null || !transactionElem.isInteger())
            return null;

        String uid = uidElem.stringValue();
        int transaction = transactionElem.intValue();
        Ant ant = executor.ants.get(uid);
        if (ant == null)
            return new ResponseJson(new JsonNull(null));
        if (ant.transaction >= transaction)
            return new ResponseJson(new JsonBoolean(null, false));
        ant.transaction = transaction;
        ant.timestamp = executor.getTicks();

        JsonObject result = new JsonObject(null);
        synchronized (ant) {
            JsonElement processedElem = obj.get("processed");
            if (processedElem != null) {
                JsonArray processedArray = processedElem.toJsonArray();
                if (processedArray != null) {
                    count = processedArray.size();
                    if (count > 0) {
                        Set<String> processedSet = new TreeSet<>();
                        for (k = 0; k < count; k++) {
                            String processedId = processedArray.getAt(k).stringValue();
                            processedSet.add(processedId);
                        }
                        List<Instruction> tmp = new LinkedList<>();
                        for (Instruction instruction : ant.instructions) {
                            if (!processedSet.contains(instruction.getUId())) {
                                tmp.add(instruction);
                            }
                        }
                        ant.instructions = tmp;
                    }
                }
            }

            JsonElement eventsElem = obj.get("events");
            if (eventsElem != null) {
                JsonArray eventsArray = eventsElem.toJsonArray();
                if (eventsArray != null) {
                    count = eventsArray.size();
                    for (k = 0; k < count; k++) {
                        JsonObject event = eventsArray.getAt(k).toJsonObject();
                        assert (event != null);
                        int widgetId = event.get("id").intValue();
                        Widget widget = ant.widgets.get(widgetId);
                        if (widget != null) {
                            widget.handleEvent(event.get("type").stringValue(), event);
                        }
                    }
                }
            }

            result.createNumber("transaction", transaction);
            count = ant.instructions.size();
            if (count > maxInstructionsCount) count = maxInstructionsCount;
            if (count > 0) {
                JsonArray jsonInstructions = result.createArray("instructions");
                k = 0;
                Iterator<Instruction> iterator = ant.instructions.iterator();
                while (k < count && iterator.hasNext()) {
                    k++;
                    Instruction instruction = iterator.next();
                    JsonObject jsonInstruction = jsonInstructions.createObject();
                    instruction.toJsonObject(jsonInstruction);
                }
            }
        }
        return new ResponseJson(result);
    }

    private static final int maxInstructionsCount = 1024;
}
