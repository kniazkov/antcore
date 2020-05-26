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

import com.kniazkov.antcore.lib.ByteBuffer;
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
    public CompilationUnit(StaticDataBuilder staticData) {
        instructions = new ArrayList<>();
        this.staticData = staticData;
        staticDataOffset = new Reference<>(0);
        dynamicDataOffset = new Reference<>(0);
    }

    public Reference<Integer> getStaticDataOffset() {
        return staticDataOffset;
    }

    public Reference<Integer> getDynamicDataOffset() {
        return dynamicDataOffset;
    }

    public ByteList getBytecode() {
        int count = instructions.size();
        int size = count * 16 + staticData.getSize();
        ByteBuffer buff = new ByteBuffer(size);

        // code
        for (int k = 0; k < count; k++) {
            instructions.get(k).generate().write(buff, k * 16);
        }

        // data
        staticData.build(buff, staticDataOffset.value);
        return buff;
    }

    private void updateOffsets() {
        staticDataOffset.value = instructions.size() * 16;
        dynamicDataOffset.value = staticDataOffset.value + staticData.getSize();
    }

    public void addInstruction(RawInstruction item) {
        instructions.add(item);
        int count = instructions.size();
        item.setIndex(count);
        updateOffsets();
    }

    public int getStringOffset(String string) {
        int offset = staticData.getStringOffset(string);
        updateOffsets();
        return offset;
    }

    private List<RawInstruction> instructions;
    private StaticDataBuilder staticData;
    private Reference<Integer> staticDataOffset;
    private Reference<Integer> dynamicDataOffset;
}
