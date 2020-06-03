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
    public static final byte CALL = 5;
    public static final byte RET = 6;
    public static final byte ENTER = 7;
    public static final byte LEAVE = 8;
    public static final byte ADD = 9;
    public static final byte CMP = 10;
    public static final byte END = 127;
}
