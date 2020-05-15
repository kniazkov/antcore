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

import com.kniazkov.antcore.lib.ByteList;

/**
 * Set of bytes that represents an instruction. Each instruction takes 16 bytes.
 */
public class Code {
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
    public void write(byte[] buff, int index) {
        buff[index] =      opcode;
        buff[index + 1] =  p0;
        buff[index + 2] =  p1;
        buff[index + 3] =  p2;

        buff[index + 4] =  (byte)(x0);
        buff[index + 5] =  (byte)(x0 >> 8);
        buff[index + 6] =  (byte)(x0 >> 16);
        buff[index + 7] =  (byte)(x0 >> 24);

        buff[index + 8] =  (byte)(x1);
        buff[index + 9] =  (byte)(x1 >> 8);
        buff[index + 10] = (byte)(x1 >> 16);
        buff[index + 11] = (byte)(x1 >> 24);

        buff[index + 12] = (byte)(x2);
        buff[index + 13] = (byte)(x2 >> 8);
        buff[index + 14] = (byte)(x2 >> 16);
        buff[index + 15] = (byte)(x2 >> 24);
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
        x0 = (int)(buff.get(index + 4)) + ((int)(buff.get(index + 5)) << 8)
                + ((int)(buff.get(index + 6)) << 16) + ((int)(buff.get(index + 7)) << 24);
        x1 = (int)(buff.get(index + 8)) + ((int)(buff.get(index + 9)) << 8)
                + ((int)(buff.get(index + 10)) << 16) + ((int)(buff.get(index + 11)) << 24);
        x2 = (int)(buff.get(index + 12)) + ((int)(buff.get(index + 13)) << 8)
                + ((int)(buff.get(index + 14)) << 16) + ((int)(buff.get(index + 15)) << 24);
    }
}
