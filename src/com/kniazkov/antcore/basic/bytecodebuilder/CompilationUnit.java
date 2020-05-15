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

import com.kniazkov.antcore.lib.ByteArrayWrapper;
import com.kniazkov.antcore.lib.ByteList;
import com.kniazkov.antcore.lib.Reference;

import java.util.ArrayList;
import java.util.List;

/**
 * The compilation unit (module with external functions)
 */
public class CompilationUnit {
    public CompilationUnit() {
        instructions = new ArrayList<>();
        dataOffset = new Reference<>();
    }

    public void addInstruction(RawInstruction item) {
        instructions.add(item);
        int count = instructions.size();
        item.setIndex(count);
        dataOffset.value = count * 16;
    }

    public ByteList getBytecode() {
        int count = instructions.size();
        int size = count * 16;
        byte[] buff = new byte[size];
        for (int i = 0; i < count; i++) {
            instructions.get(i).generate().write(buff, i * 16);
        }
        return new ByteArrayWrapper(buff);
    }

    public Reference<Integer> getDataOffset() {
        return dataOffset;
    }

    private List<RawInstruction> instructions;
    private Reference<Integer> dataOffset;
}
