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
 * The disassembler for bytecode
 */
public class Disassembler {
    public static String convert(ByteList bytecode) {
        int size = bytecode.size();
        Code code = new Code();
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < size; index = index + 16) {
            code.read(bytecode, index);
            switch (code.opcode) {
                case 1:
                    builder.append("LOAD")
                            .append('\t').append((int)code.p0).append(' ')
                            .append('\t').append(code.x0).append(' ').append(code.x1).append(' ').append('\n');
                    break;
                case 2:
                    builder.append("STORE")
                            .append('\t').append((int)code.p0).append(' ')
                            .append('\t').append(code.x0).append(' ').append(code.x1).append(' ').append('\n');
                    break;
                default:
                    builder.append(code.opcode)
                            .append('\t').append((int)code.p0).append(' ').append((int)code.p1).append(' ').append((int)code.p2)
                            .append('\t').append(code.x0).append(' ').append(code.x1).append(' ').append(code.x2).append('\n');
            }
        }
        return builder.toString();
    }
}
