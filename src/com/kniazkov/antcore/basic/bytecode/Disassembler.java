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

import java.lang.reflect.Type;

/**
 * The disassembler for bytecode
 */
public class Disassembler {
    public static String convert(ByteList bytecode) {
        int size = bytecode.size();
        Instruction inst = new Instruction();
        StringBuilder buff = new StringBuilder();
        for (int index = 0; index < size; index = index + 16) {
            inst.read(bytecode, index);
            decoders[inst.opcode].decode(inst, buff);
            buff.append('\n');
        }
        return buff.toString();
    }

    interface Decoder {
        void decode(Instruction inst, StringBuilder buff);
    }

    final static Decoder[] decoders = {
            (i, buff) -> { // 0. NOP
                buff.append("NOP");
            },
            (i, buff) -> { // 1. LOAD
                buff.append("LOAD\t");
                switch (i.p0) {
                    case DataSelector.GLOBAL:
                        buff.append("GLOBAL\t").append(' ').append(i.x0).append(", ").append(i.x1);
                        break;
                    case DataSelector.IMMEDIATE:
                        buff.append("VAL \t").append(' ').append(i.x0).append(", ").append(i.x1);
                        if (i.x0 > 4)
                            buff.append(", ").append(i.x2);
                        break;
                }
            },
            (i, buff) -> { // 2. STORE
                buff.append("STORE\t");
                switch (i.p0) {
                    case DataSelector.GLOBAL:
                        buff.append("GLOBAL\t").append(' ').append(i.x0).append("b, ").append(i.x1);
                        break;
                }
            },
            (i, buff) -> { // 3. RET
                buff.append("RET");
            },
            (i, buff) -> { // 4. ADD
                buff.append("ADD \t");
                switch (i.p0) {
                    case TypeSelector.INTEGER:
                        buff.append("INT");
                        break;
                }
            },
    };
}
