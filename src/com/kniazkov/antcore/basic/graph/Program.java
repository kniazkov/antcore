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
package com.kniazkov.antcore.basic.graph;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * The node that represents a whole program
 */
public class Program extends Node implements DataTypeOwner {
    public Program(Map<String, Module> modules) {
        this.modules = Collections.unmodifiableMap(modules);
        for (Map.Entry<String, Module> entry : modules.entrySet()) {
            entry.getValue().setOwner(this);
        }

        Map<String, DataType> types = new TreeMap<>();
        types.put("INTEGER", new BuiltInType(this) {
            @Override
            public String getName() {
                return "INTEGER";
            }

            @Override
            public int getSize() {
                return 4;
            }
        });
        this.types = Collections.unmodifiableMap(types);
    }

    public String toSourceCode() {
        StringBuilder buff = new StringBuilder();
        toSourceCode(buff, "", "\t");
        return buff.toString();
    }

    @Override
    public Node getOwner() {
        return null;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        boolean flag = false;

        for (Map.Entry<String, Module> entry : modules.entrySet()) {
            if (flag)
                buff.append("\n");
            entry.getValue().toSourceCode(buff, i, i0);
            flag = true;
        }
    }

    private Map<String, Module> modules;
    private Map<String, DataType> types;
}
