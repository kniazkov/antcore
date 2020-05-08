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

import com.kniazkov.antcore.basic.Fragment;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * The node that represents a whole program
 */
public class Program extends Node {
    public Program(Map<String, Module> modules) {
        super(null);
        this.modules = Collections.unmodifiableMap(modules);

        this.types = new TreeMap<>();
        types.put("INTEGER", new DataType(null) {
            @Override
            public String getName() {
                return "INTEGER";
            }

            @Override
            public int getSize() {
                return 4;
            }

            @Override
            public boolean builtIn() {
                return true;
            }
        });
    }

    public String toSourceCode() {
        StringBuilder buff = new StringBuilder();
        toSourceCode(buff, "", "\t");
        return buff.toString();
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
