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

/**
 * An instruction of bytecode
 */
public abstract class RawInstruction {
    public RawInstruction() {
        index = -1;
    }

    /**
     * @return set of bytes that represents an instruction
     */
    public abstract Instruction generate();

    void setIndex(int index) {
        assert(this.index < 0);
        this.index = index;
    }

    public int getAddress() {
        assert(this.index >= 0);
        return index * 16;
    }

    private int index;
}
