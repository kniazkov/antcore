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

import com.kniazkov.antcore.basic.common.Offset;
import com.kniazkov.antcore.basic.graph.Function;
import com.kniazkov.antcore.basic.graph.Module;
import com.kniazkov.antcore.lib.ByteBuffer;
import com.kniazkov.antcore.lib.ByteList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
 The memory model:
 +------+-------------+--------------+-------+
 + CODE | STATIC DATA | DYNAMIC DATA | STACK |
 +------+-------------+--------------+-------+
 */

/**
 * The compilation unit (module with external functions)
 */
public class CompilationUnit {
    private static class SegmentOffset implements Offset {
        int value = 0;

        @Override
        public int get() {
            return value;
        }
    }

    public CompilationUnit(Module module, StaticDataBuilder staticData) {
        this.module = module;
        instructions = new ArrayList<>();
        this.staticData = staticData;
        staticDataOffset = new SegmentOffset();
        dynamicDataOffset = new SegmentOffset();
        notCompiledButUsedFunctions = new LinkedList<>();
    }

    public Module getModule() {
        return module;
    }

    public Offset getStaticDataOffset() {
        return staticDataOffset;
    }

    public Offset getDynamicDataOffset() {
        return dynamicDataOffset;
    }

    public void addNotCompiledFunction(Function function) {
        notCompiledButUsedFunctions.addLast(function);
    }

    public Function getNextNotCompiledFunction() {
        return notCompiledButUsedFunctions.isEmpty() ? null : notCompiledButUsedFunctions.removeFirst();
    }

    /**
     * Generate binary code
     * @return a bytecode as list of bytes
     */
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

    private void updateSegmentOffsets() {
        staticDataOffset.value = instructions.size() * 16;
        dynamicDataOffset.value = staticDataOffset.value + staticData.getSize();
    }

    public void addInstruction(RawInstruction item) {
        int count = instructions.size();
        instructions.add(item);
        item.setIndex(count);
        updateSegmentOffsets();
    }

    public Offset getStringOffset(String string) {
        Offset offset = staticData.getStringOffset(string);
        updateSegmentOffsets();
        return offset;
    }

    private Module module;
    private List<RawInstruction> instructions;
    private StaticDataBuilder staticData;
    private SegmentOffset staticDataOffset;
    private SegmentOffset dynamicDataOffset;
    private LinkedList<Function> notCompiledButUsedFunctions;
}
