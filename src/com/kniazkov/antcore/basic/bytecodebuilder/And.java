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

import com.kniazkov.antcore.basic.bytecode.Instruction;
import com.kniazkov.antcore.basic.bytecode.OpCode;
import com.kniazkov.antcore.basic.bytecode.TypeSelector;

/**
 * Load two values from the stack, perform the conjunction and push the result to the stack
 */
public class And extends RawInstruction {
    public And(byte selector, int leftSize, int rightSize, int totalSize) {
        this.selector = selector;
        assert (selector >= TypeSelector.BYTE && selector <= TypeSelector.LONG );
        this.leftSize = leftSize;
        this.rightSize = rightSize;
        this.totalSize = totalSize;
    }

    @Override
    public Instruction generate() {
        Instruction i = new Instruction();
        i.opcode = OpCode.AND;
        i.p0 = selector;
        i.x0 = leftSize;
        i.x1 = rightSize;
        i.x2 = totalSize;
        return i;
    }

    private byte selector;
    private int leftSize;
    private int rightSize;
    private int totalSize;
}
