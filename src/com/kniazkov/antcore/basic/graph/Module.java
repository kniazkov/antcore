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

/**
 * The node that represents a module
 */
public class Module implements  Node {
    public Module(String name, String executor) {
        this.name = name;
        this.executor = executor;
    }

    public String getName() {
        return name;
    }

    public String getExecutor() {
        return executor;
    }

    @Override
    public void print(List<String> dst, String i, String i0) {
        if (executor != null)
            dst.add(i + "MODULE " + name + " " + executor);
        else
            dst.add(i + "MODULE " + name);
        dst.add(i + "END MODULE");
    }

    private String name;
    private String executor;
}
