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
import com.kniazkov.antcore.basic.bytecode.DataSelector;
import com.kniazkov.antcore.basic.bytecode.OpCode;
import com.kniazkov.antcore.lib.Reference;

/**
 * Store a value to the memory
 */
public class Store extends RawInstruction {
    public Store(byte selector, int size, Reference<Integer> offset) {
        this.selector = selector;
        this.size = size;
        this.offset = offset;
        this.address = new Reference<>();
    }

    public Store(byte selector, int size, Reference<Integer> offset, int address) {
        this.selector = selector;
        this.size = size;
        this.offset = offset;
        this.address = new Reference<>(address);
    }

    @Override
    public Instruction generate() {
        Instruction i = new Instruction();
        i.opcode = OpCode.STORE;
        i.p0 = selector;
        i.x0 = size;
        i.x1 = offset.value + address.value;
        return i;
    }

    public Reference<Integer> getAddressReference() {
        return address;
    }

    private byte selector;
    private int size;
    private Reference<Integer> offset;
    private Reference<Integer> address;
}
