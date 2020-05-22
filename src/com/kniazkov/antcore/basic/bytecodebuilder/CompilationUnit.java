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
package com.kniazkov.antcore.basic.bytecodebuilder;

import com.kniazkov.antcore.lib.ByteList;
import com.kniazkov.antcore.lib.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The compilation unit (module with external functions)
 */
public class CompilationUnit {
    public CompilationUnit() {
        instructions = new ArrayList<>();
        staticDataOffset = new Reference<>(0);
        staticDataSize = 0;
        dynamicDataOffset = new Reference<>(0);
        stringsList = new ArrayList<>();
        stringsMap = new TreeMap<>();
    }

    public Reference<Integer> getStaticDataOffset() {
        return staticDataOffset;
    }

    public Reference<Integer> getDynamicDataOffset() {
        return dynamicDataOffset;
    }

    public ByteList getBytecode() {
        int count = instructions.size();
        int size = count * 16 + staticDataSize;
        byte[] buff = new byte[size];

        // code
        for (int k = 0; k < count; k++) {
            instructions.get(k).generate().write(buff, k * 16);
        }

        // strings
        for (String string : stringsList) {
            int index = stringsMap.get(string) + staticDataOffset.value;
            int length = string.length();
            // current length
            buff[index] =      (byte)(length);
            buff[index + 1] =  (byte)(length >> 8);
            buff[index + 2] =  (byte)(length >> 16);
            buff[index + 3] =  (byte)(length >> 24);
            // capacity, the same as length
            buff[index + 4] =  buff[index];
            buff[index + 5] =  buff[index + 1];
            buff[index + 6] =  buff[index + 2];
            buff[index + 7] =  buff[index + 3];
            // data
            for (int k = 0; k < length; k++) {
                char ch = string.charAt(k);
                buff[index + 8 + k * 2] = (byte) (ch & 0xff);
                buff[index + 8 + k * 2 + 1] = (byte) (ch >> 8);
            }
        }
        return ByteList.wrap(buff);
    }

    private void updateOffsets() {
        staticDataOffset.value = instructions.size() * 16;
        dynamicDataOffset.value = staticDataOffset.value + staticDataSize;
    }

    public void addInstruction(RawInstruction item) {
        instructions.add(item);
        int count = instructions.size();
        item.setIndex(count);
        updateOffsets();
    }

    public int getStringOffset(String string) {
        if (stringsMap.containsKey(string))
            return stringsMap.get(string);
        int offset = staticDataSize;
        staticDataSize += string.length() * 2 + 8;
        stringsList.add(string);
        stringsMap.put(string, offset);
        updateOffsets();
        return offset;
    }

    private List<RawInstruction> instructions;
    private Reference<Integer> staticDataOffset;
    private int staticDataSize;
    private Reference<Integer> dynamicDataOffset;
    private List<String> stringsList;
    private Map<String, Integer> stringsMap;
}
