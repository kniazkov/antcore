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
 * Leave function, i.e. restore stack and then old local pointer
 */
public class Leave extends RawInstruction {
    public Leave(int size) {
        this.size = size;
    }

    @Override
    public Instruction generate() {
        Instruction i = new Instruction();
        i.opcode = OpCode.LEAVE;
        i.x0 = size;
        return i;
    }

    protected int size;
}
