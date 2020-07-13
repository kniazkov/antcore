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

import com.kniazkov.antcore.basic.common.FixedOffset;
import com.kniazkov.antcore.basic.common.Offset;
import com.kniazkov.antcore.basic.graph.Module;
import com.kniazkov.antcore.lib.ByteBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The class for building static data in binary form
 */
public class StaticDataBuilder {
    public StaticDataBuilder(Module module) {
        this.module = module;
        staticDataSize = 0;
        stringsList = new ArrayList<>();
        stringsMap = new TreeMap<>();
    }

    /**
     * Calculate a static string offset
     * @param string the string
     * @return an offset
     */
    public Offset getStringOffset(String string) {
        if (stringsMap.containsKey(string))
            return stringsMap.get(string);
        Offset offset = new FixedOffset(staticDataSize);
        staticDataSize += string.length() * 2 + 8;
        stringsList.add(string);
        stringsMap.put(string, offset);
        return offset;
    }

    /**
     * Build binary data
     * @param buff the destination buffer
     * @param segmentOffset offset relative to the beginning of the buffer
     */
    public void build(ByteBuffer buff, int segmentOffset) {
        for (String string : stringsList) {
            int index = stringsMap.get(string).get() + segmentOffset;
            int length = string.length();
            // current length
            buff.setInt(index, length);
            // capacity, the same as length
            buff.setInt(index + 4, length);
            // data
            for (int k = 0; k < length; k++) {
                char ch = string.charAt(k);
                buff.setChar(index + 8 + k * 2, ch);
            }
        }
    }

    public Module getModule() {
        return module;
    }

    public int getSize() {
        return staticDataSize;
    }

    private Module module;
    private int staticDataSize;
    private List<String> stringsList;
    private Map<String, Offset> stringsMap;
}
