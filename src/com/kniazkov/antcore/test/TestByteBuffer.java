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
package com.kniazkov.antcore.test;

import com.kniazkov.antcore.lib.ByteBuffer;

/**
 * Tests for ByteBuffer class
 */
public class TestByteBuffer {
    public static void main(String[] args) {
        ByteBuffer buff = new ByteBuffer(12);
        buff.setInt(0, 132);
        buff.setInt(4, -1234567);
        System.out.println(buff.getInt(0));
        System.out.println(buff.getInt(4));
        buff.setChar(8, 'A');
        buff.setChar(10, 'Ж');
        System.out.println(buff.getChar(8));
        System.out.println(buff.getChar(10));
    }
}
