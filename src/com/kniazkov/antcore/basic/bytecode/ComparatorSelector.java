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
 * The list of comparator selectors
 */
public final class ComparatorSelector {
    public static final byte EQUAL = 0;
    public static final byte DIFF = 1;
    public static final byte LESS = 2;
    public static final byte LESS_EQUAL = 3;
    public static final byte GREATER = 4;
    public static final byte GREATER_EQUAL = 5;


    public static String toString(byte selector) {
        switch(selector) {
            case EQUAL:
                return "EQ  ";
            case DIFF:
                return "DIFF";
            case LESS:
                return "LESS";
            case LESS_EQUAL:
                return "LEQ ";
            case GREATER:
                return "GT  ";
            case GREATER_EQUAL:
                return "GEQ ";
        }
        return "?";
    }
}
