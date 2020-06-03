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
 * Compare two values on the stack and push boolean result to the stack
 */
public class Compare extends RawInstruction {
    public Compare(byte typeSelector, byte comparatorSelector, int leftSize, int rightSize) {
        this.typeSelector = typeSelector;
        this.comparatorSelector = comparatorSelector;
        this.leftSize = leftSize;
        this.rightSize = rightSize;
    }

    @Override
    public Instruction generate() {
        Instruction i = new Instruction();
        i.opcode = OpCode.CMP;
        i.p0 = typeSelector;
        i.p1 = comparatorSelector;
        i.x0 = leftSize;
        i.x1 = rightSize;
        return i;
    }

    private byte typeSelector;
    private byte comparatorSelector;
    private int leftSize;
    private int rightSize;
}
