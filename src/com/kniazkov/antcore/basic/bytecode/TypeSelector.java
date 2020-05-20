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
package com.kniazkov.antcore.basic.bytecode;

/**
 * The list of type selectors
 */
public final class TypeSelector {
    public static final byte UNKNOWN = 0;
    public static final byte POINTER = 1;
    public static final byte BOOLEAN = 2;
    public static final byte BYTE = 3;
    public static final byte SHORT = 4;
    public static final byte INTEGER = 5;
    public static final byte LONG = 6;
    public static final byte REAL = 7;
    public static final byte STRING = 8;
    public static final byte ARRAY = 9;
    public static final byte STRUCT = 10;

    public static String toString(byte selector) {
        switch(selector) {
            case POINTER:
                return "PTR";
            case BOOLEAN:
                return "BOOL";
            case BYTE:
                return "BYTE";
            case SHORT:
                return "SHORT";
            case INTEGER:
                return "INT";
            case LONG:
                return "LONG";
            case REAL:
                return "REAL";
            case STRING:
                return "STRING";
            case ARRAY:
                return "ARRAY";
            case STRUCT:
                return "STRUCT";
        }
        return "?";
    }
}
