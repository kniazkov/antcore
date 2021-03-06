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

/**
 * Cast value to another type
 */
public class Cast extends RawInstruction {
    public Cast(byte currentType, int currentSize, byte newType, int newSize) {
        this.currentType = currentType;
        this.currentSize = currentSize;
        this.newType = newType;
        this.newSize = newSize;
    }

    @Override
    public Instruction generate() {
        Instruction i = new Instruction();
        i.opcode = OpCode.CAST;
        i.p0 = currentType;
        i.x0 = currentSize;
        i.p1 = newType;
        i.x1 = newSize;
        return i;
    }

    private byte currentType;
    private int currentSize;
    private byte newType;
    private int newSize;
}
