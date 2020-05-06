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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * The node that represents a whole program
 */
public class Program implements Node {
    public Program() {
        modules = new TreeMap<>();
    }

    public String toSourceCode() {
        List<String> listing = new ArrayList<>();
        print(listing, "", "  ");
        StringBuilder builder = new StringBuilder();
        for (String line : listing) {
            builder.append(line);
            builder.append("\n");
        }
        return builder.toString();
    }

    public boolean hasModule(String name) {
        return modules.containsKey(name);
    }

    public void addModule(Module module) {
        modules.put(module.getName(), module);
    }

    @Override
    public void print(List<String> dst, String i, String i0) {
        boolean flag = false;
        for (Map.Entry<String, Module> entry : modules.entrySet()) {
            if (flag)
                dst.add("");
            entry.getValue().print(dst, i, i0);
            flag = true;
        }
    }

    private Map<String, Module> modules;
}
