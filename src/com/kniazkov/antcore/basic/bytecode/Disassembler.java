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
    public static String convert(ByteList src) {
        int size = src.size();
        Instruction inst = new Instruction();
        StringBuilder dst = new StringBuilder();
        for (int index = 0; index < size; index = index + 16) {
            inst.read(src, index);
            dst.append(index).append('\t');
            decoders[inst.opcode].decode(inst, dst);
            dst.append('\n');
            if (inst.opcode == OpCode.END)
                break;
        }
        return dst.toString();
    }

    interface Decoder {
        void decode(Instruction inst, StringBuilder buff);
    }

    final static Decoder stub = (i, buff) -> {

    };

    final static Decoder[] decoders = {
            (i, buff) -> { // 0 -> NOP
                buff.append("NOP");
            },
            (i, buff) -> { // 1 -> LOAD
                buff.append("LOAD\t");
                switch (i.p0) {
                    case DataSelector.GLOBAL:
                    case DataSelector.LOCAL:
                    case DataSelector.LOCAL_POINTER:
                        buff.append(DataSelector.toString(i.p0)).append('\t').append(i.x0).append(", ").append(i.x1);
                        break;
                    case DataSelector.IMMEDIATE:
                        buff.append("VAL \t").append(i.x0).append(", ").append(i.x1);
                        if (i.x0 > 4)
                            buff.append(", ").append(i.x2);
                        break;
                }
            },
            (i, buff) -> { // 2 -> STORE
                buff.append("STORE\t").append(DataSelector.toString(i.p0)).append('\t').append(i.x0).append(", ").append(i.x1);
            },
            (i, buff) -> { // 3 -> CAST
                buff.append("CAST \t").append(TypeSelector.toString(i.p0)).append(" ").append(i.x0).append(" -> ")
                        .append(TypeSelector.toString(i.p1)).append(" ").append(i.x1);
            },
            (i, buff) -> { // 4 -> POP
                buff.append("POP \t").append(i.x0);
            },
            (i, buff) -> { // 5 -> CALL
                buff.append("CALL\t").append(FunctionSelector.toString(i.p0)).append('\t').append(i.x0);
            },
            (i, buff) -> { // 6 -> RET
                buff.append("RET");
            },
            (i, buff) -> { // 7 -> ENTER
                buff.append("ENTER\t").append(i.x0);
            },
            (i, buff) -> { // 8 -> LEAVE
                buff.append("LEAVE\t").append(i.x0);
            },
            (i, buff) -> { // 9 -> ADD
                buff.append("ADD \t").append(TypeSelector.toString(i.p0)).append('\t')
                        .append(i.x0).append(", ").append(i.x1).append(" -> ").append(i.x2);
            },
            stub, // 10
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub, // 20
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub, // 30
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub, // 40
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub, // 50
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub, // 60
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub, // 70
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub, // 80
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub, // 90
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub, // 100
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub, // 110
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            stub, // 120
            stub,
            stub,
            stub,
            stub,
            stub,
            stub,
            (i, buff) -> { // 127 -> END
                buff.append("END");
            },
    };
}
