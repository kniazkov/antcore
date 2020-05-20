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

/**
 * String that can be converted from/to byte array
 */
public class StringData {
    public int length;
    public int capacity;
    public byte[] data;

    @Override
    public String toString() {
        if (length == 0)
            return "";

        StringBuilder buff = new StringBuilder(length);
        for (int k = 0; k < length * 2 && k < data.length; k += 2) {
            char c = (char) ((int)data[k] + ((int)data[k + 1] << 8));
            buff.append(c);
        }
        return buff.toString();
    }
}
