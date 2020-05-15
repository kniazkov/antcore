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

import com.kniazkov.antcore.lib.Reference;

/**
 * Store a value to the memory
 */
public class Store extends Instruction {
    public Store(DataSelector selector, int size) {
        this.selector = selector;
        this.address = new Reference<>();
        this.size = size;
    }

    public Store(DataSelector selector, int address, int size) {
        this.selector = selector;
        this.address = new Reference<>(address);
        this.size = size;
    }

    @Override
    public Code getCode() {
        Code c = new Code();
        c.opcode = OpCode.STORE.getValue();
        c.p0 = selector.getValue();
        c.x0 = size;
        c.x1 = address.value;
        return c;
    }

    public Reference<Integer> getAddressReference() {
        return address;
    }

    private DataSelector selector;
    private Reference<Integer> address;
    private int size;
}
