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
        Formatter formatter = new Formatter();
        for (int address = 0; address < size; address = address + 16) {
            inst.read(src, address);
            decoders[inst.opcode].decode(address, inst, formatter);
            if (inst.opcode == OpCode.END)
                break;
        }
        return formatter.toString();
    }

    static class Formatter {
        public Formatter() {
            buff = new StringBuilder();
        }

        @Override
        public String toString() {
            return buff.toString();
        }

        public void add(int address, String oper) {
            append(String.valueOf(address), 8).append(oper)
                    .append('\n');
        }

        public void add(int address, String oper, String p0) {
            append(String.valueOf(address), 8).append(oper, 8)
                    .append(p0).append('\n');
        }

        public void add(int address, String oper, String p0, String p1, String p2, int x0) {
            append(String.valueOf(address), 8).append(oper, 8)
                    .append(p0, 8).append(p1, 8).append(p2, 8)
                    .append(x0)
                    .append('\n');
        }

        public void add(int address, String oper, String p0, String p1, String p2, int x0, int x1) {
            append(String.valueOf(address), 8).append(oper, 8)
                    .append(p0, 8).append(p1, 8).append(p2, 8)
                    .append(x0).append(", ").append(x1)
                    .append('\n');
        }

        public void add(int address, String oper, String p0, String p1, String p2, int x0, int x1, int x2) {
            append(String.valueOf(address), 8).append(oper, 8)
                    .append(p0, 8).append(p1, 8).append(p2, 8)
                    .append(x0).append(", ").append(x1).append(", ").append(x2)
                    .append('\n');
        }

        private Formatter append(String string, int tab) {
            int k = 0;
            if (string != null) {
                k = string.length();
                buff.append(string);
            }
            while(k != tab) {
                buff.append(' ');
                k++;
            }
            return this;
        }

        private Formatter append(String string) {
            buff.append(string);
            return this;
        }

        private Formatter append(int intValue) {
            buff.append(intValue);
            return this;
        }

        private Formatter append(char charValue) {
            buff.append(charValue);
            return this;
        }

        private StringBuilder buff;
    }

    interface Decoder {
        void decode(int address, Instruction inst, Formatter buff);
    }

    final static Decoder stub = (a, i, buff) -> {

    };

    final static Decoder[] decoders = {
            (a, i, buff) -> { // 0 -> NOP
                buff.add(a, "NOP");
            },
            (a, i, buff) -> { // 1 -> LOAD
                switch (i.p0) {
                    case DataSelector.GLOBAL:
                    case DataSelector.LOCAL:
                    case DataSelector.LOCAL_POINTER:
                        buff.add(a, "LOAD", DataSelector.toString(i.p0), null, null, i.x0, i.x1);
                        break;
                    case DataSelector.IMMEDIATE:
                        if (i.x0 > 4)
                            buff.add(a, "LOAD", "VALUE", null, null, i.x0, i.x1, i.x2);
                        else
                            buff.add(a, "LOAD", "VALUE", null, null, i.x0, i.x1);
                        break;
                    case DataSelector.ZERO:
                        buff.add(a, "LOAD", "ZERO", null, null, i.x0);
                        break;
                }
            },
            (a, i, buff) -> { // 2 -> STORE
                buff.add(a, "STORE", DataSelector.toString(i.p0), null, null, i.x0, i.x1);
            },
            (a, i, buff) -> { // 3 -> CAST
                buff.add(a, "CAST", TypeSelector.toString(i.p0), TypeSelector.toString(i.p1), null, i.x0, i.x1);
            },
            (a, i, buff) -> { // 4 -> POP
                buff.add(a, "POP", null, null, null, i.x0);
            },
            (a, i, buff) -> { // 5 -> DUP
                buff.add(a, "DUP", null, null, null, i.x0);
            },
            (a, i, buff) -> { // 6 -> CALL
                buff.add(a, "CALL", FunctionSelector.toString(i.p0), null, null, i.x0);
            },
            (a, i, buff) -> { // 7 -> RET
                buff.add(a, "RET");
            },
            (a, i, buff) -> { // 8 -> ENTER
                buff.add(a, "ENTER", null, null, null, i.x0);
            },
            (a, i, buff) -> { // 9 -> LEAVE
                buff.add(a, "LEAVE", null, null, null, i.x0);
            },
            (a, i, buff) -> { // 10 -> ADD
                buff.add(a, "ADD", TypeSelector.toString(i.p0), null, null, i.x0, i.x1, i.x2);
            },
            (a, i, buff) -> { // 11 -> SUB
                buff.add(a, "SUB", TypeSelector.toString(i.p0), null, null, i.x0, i.x1, i.x2);
            },
            (a, i, buff) -> { // 12 -> MUL
                buff.add(a, "MUL", TypeSelector.toString(i.p0), null, null, i.x0, i.x1, i.x2);
            },
            (a, i, buff) -> { // 13 -> DIV
                buff.add(a, "DIV", TypeSelector.toString(i.p0), null, null, i.x0, i.x1, i.x2);
            },
            (a, i, buff) -> { // 14 -> MOD
                buff.add(a, "MOD", TypeSelector.toString(i.p0), null, null, i.x0, i.x1, i.x2);
            },
            (a, i, buff) -> { // 15 -> AND
                buff.add(a, "AND", TypeSelector.toString(i.p0), null, null, i.x0, i.x1, i.x2);
            },
            (a, i, buff) -> { // 16 -> OR
                buff.add(a, "OR", TypeSelector.toString(i.p0), null, null, i.x0, i.x1, i.x2);
            },
            (a, i, buff) -> { // 17 -> XOR
                buff.add(a, "XOR", TypeSelector.toString(i.p0), null, null, i.x0, i.x1, i.x2);
            },
            (a, i, buff) -> { // 18 -> CMP
                buff.add(a, "CMP", TypeSelector.toString(i.p0), ComparatorSelector.toString(i.p1), null,
                        i.x0, i.x1);
            },
            (a, i, buff) -> { // 19 -> SIGN
                buff.add(a, "SIGN", TypeSelector.toString(i.p0));
            },
            (a, i, buff) -> { // 20 -> IF
                buff.add(a, "IF", i.p0 > 0 ? "TRUE" : "FALSE", null, null, i.x0);
            },
            (a, i, buff) -> { // 21 -> JUMP
                buff.add(a, "JUMP", null, null, null, i.x0);
            },
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
            (a, i, buff) -> { // 127 -> END
                buff.add(a, "END");
            },
    };
}
