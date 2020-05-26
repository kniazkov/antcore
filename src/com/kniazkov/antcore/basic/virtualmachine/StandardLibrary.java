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
package com.kniazkov.antcore.basic.virtualmachine;

import java.util.Map;
import java.util.TreeMap;

/**
 * Built-in functions
 */
public class StandardLibrary {
    public static Map<String, NativeFunction> getFunctions() {
        if (functions == null) {
            functions = new TreeMap<>();
            functions.put("print", (memory, SP) -> {
                int address = memory.getInt(SP + 4);
                StringData string = StringData.read(memory, address);
                System.out.println(string);
            });
        }
        return functions;
    }

    private static Map<String, NativeFunction> functions;
}
