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

import com.kniazkov.antcore.basic.bytecode.DataSelector;
import com.kniazkov.antcore.basic.bytecode.Instruction;
import com.kniazkov.antcore.basic.bytecode.OpCode;
import com.kniazkov.antcore.lib.FixedPoint;

/**
 * Load a real number value to the stack
 */
public class PushReal extends RawInstruction {
    public PushReal(FixedPoint value) {
        this.value = value;
    }

    @Override
    public Instruction generate() {
        Instruction i = new Instruction();
        i.opcode = OpCode.LOAD;
        i.p0 = DataSelector.IMMEDIATE;
        i.x0 = 8;
        long data = value.getFixedAsLong();
        i.x1 = (int)(data);
        i.x2 = (int)(data >> 32);
        return i;
    }

    protected FixedPoint value;
}
