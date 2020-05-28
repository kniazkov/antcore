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

import com.kniazkov.antcore.lib.ByteBuffer;

/**
 * String that can be converted from/to byte array
 */
public class StringData {
    public int length;
    public int capacity;
    public byte[] data;

    public static StringData read(ByteBuffer memory, int address) {
        StringData string = new StringData();
        string.length = memory.getInt(address);
        string.capacity = memory.get(address + 4);
        string.data = new byte[string.length * 2];
        memory.copy(address + 8, string.data, 0, string.length * 2);
        return string;
    }

    public void write(ByteBuffer memory, int address) {
        memory.setInt(address, length);
        memory.setInt(address + 4, capacity);
        memory.setArray(address + 8, data, 0, length * 2);
    }

    public static void concat(StringData result, StringData str1, StringData str2) {
        result.length = str1.length + str2.length;
        if (result.length > result.capacity)
            result.length = result.capacity;
        result.data = new byte[result.length * 2];
        if (result.length >= str1.length) {
            System.arraycopy(str1.data, 0, result.data, 0, str1.length * 2);
            if (result.length == str1.length + str2.length)
                System.arraycopy(str2.data, 0, result.data, str1.length * 2, str2.length * 2);
            else
                System.arraycopy(str2.data, 0, result.data, str1.length * 2,
                        (result.length - str1.length) * 2);
        }
        else {
            System.arraycopy(str1.data, 0, result.data, 0, result.length * 2);
        }
    }

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
