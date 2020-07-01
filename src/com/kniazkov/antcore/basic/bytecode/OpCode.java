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

/**
 * The list of opcodes
 */
public final class OpCode {
    public static final byte NOP = 0;
    public static final byte LOAD = 1;
    public static final byte STORE = 2;
    public static final byte CAST = 3;
    public static final byte POP = 4;
    public static final byte DUP = 5;
    public static final byte CALL = 6;
    public static final byte RET = 7;
    public static final byte ENTER = 8;
    public static final byte LEAVE = 9;
    public static final byte ADD = 10;
    public static final byte SUB = 11;
    public static final byte MUL = 12;
    public static final byte DIV = 13;
    public static final byte MOD = 14;
    public static final byte CMP = 15;
    public static final byte SIGN = 16;
    public static final byte IF = 17;
    public static final byte JUMP = 18;
    public static final byte END = 127;
}
