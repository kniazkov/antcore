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
package com.kniazkov.antcore.basic.bytecode;

import com.kniazkov.antcore.lib.ByteBuffer;
import com.kniazkov.antcore.lib.ByteList;

/**
 * Set of bytes that represents an instruction. Each instruction takes 16 bytes.
 */
public class Instruction {
    public byte opcode;

    public byte p0;
    public byte p1;
    public byte p2;

    public int x0;
    public int x1;
    public int x2;

    /**
     * Write bytecode to the buffer
     * @param buff the buffer
     * @param index the starting index
     */
    public void write(ByteBuffer buff, int index) {
        buff.set(index, opcode);
        buff.set(index + 1, p0);
        buff.set(index + 2, p1);
        buff.set(index + 3, p2);
        buff.setInt(index + 4, x0);
        buff.setInt(index + 8, x1);
        buff.setInt(index + 12, x2);
    }

    /**
     * Read bytecode from the buffer
     * @param buff the buffer
     * @param index the starting index
     */
    public void read(ByteList buff, int index) {
        opcode = buff.get(index);
        p0 = buff.get(index + 1);
        p1 = buff.get(index + 2);
        p2 = buff.get(index + 3);
        x0 = buff.getInt(index + 4);
        x1 = buff.getInt(index + 8);
        x2 = buff.getInt(index + 12);
    }
}
