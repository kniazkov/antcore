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
package com.kniazkov.antcore.basic.virtualmachine;

import com.kniazkov.antcore.basic.bytecode.DataSelector;
import com.kniazkov.antcore.lib.ByteList;

/**
 * The virtual machine that runs compiled code
 */
public class VirtualMachine {
    public VirtualMachine(ByteList code, int memorySize) {
        memory = new byte[memorySize];
        code.copy(0, code.size(), memory, 0);
    }

    /**
     * Starts execution
     */
    public void run() {
        IP = 0;
        SP = memory.length;
        power = true;
        while(power) {
            units[readOpcode()].exec();
        }
    }

    byte[] memory;
    boolean power;
    int IP;
    int SP;

    final byte readOpcode() {
        return memory[IP];
    }

    final byte read_p0() {
        return memory[IP + 1];
    }

    final byte read_p1() {
        return memory[IP + 2];
    }

    final byte read_p2() {
        return memory[IP + 3];
    }

    final int read_x0() {
        return (int)(memory[IP + 4]) + ((int)(memory[IP + 5]) << 8)
                + ((int)(memory[IP + 6]) << 16) + ((int)(memory[IP + 7]) << 24);
    }

    final int read_x1() {
        return (int)(memory[IP + 8]) + ((int)(memory[IP + 9]) << 8)
                + ((int)(memory[IP + 10]) << 16) + ((int)(memory[IP + 11]) << 24);
    }

    final int read_x2() {
        return (int)(memory[IP + 12]) + ((int)(memory[IP + 13]) << 8)
                + ((int)(memory[IP + 14]) << 16) + ((int)(memory[IP + 15]) << 24);
    }

    final void move(int fromPos, int toPos, int size) {
        System.arraycopy(memory, fromPos, memory, toPos, size);
    }

    final void pushInteger(int value) {
        SP = SP - 4;
        memory[SP] =      (byte)(value);
        memory[SP + 1] =  (byte)(value >> 8);
        memory[SP + 2] =  (byte)(value >> 16);
        memory[SP + 3] =  (byte)(value >> 24);
    }

    final int popInteger() {
        int value = (int)(memory[SP]) + ((int)(memory[SP + 1]) << 8)
                + ((int)(memory[SP + 2]) << 16) + ((int)(memory[SP + 3]) << 24);
        SP = SP + 4;
        return value;
    }

    interface Load {
        void exec(int size);
    }

    final Load[] load = {
            (size) -> { // 0 -> GLOBAL
                move(read_x1(), SP, size);
            },
            (size) -> { // 1 -> INSTRUCTION
                move(IP + 8, SP, size);
            },
    };

    interface Store {
        void exec(int size);
    }

    final Store[] store = {
            (size) -> { // 0 -> GLOBAL
                move(SP, read_x1(), size);
            },
    };

    interface Add {
        void exec();
    }

    final Add[] add = {
            () -> { // 0 -> INTEGER
                pushInteger(popInteger() + popInteger());
            },
    };

    interface Unit {
        void exec();
    }

    final Unit[] units = {
            () -> { // 0 -> NOP
                IP = IP + 16;
            },
            () -> { // 1 -> LOAD
                int size = read_x0();
                SP = SP - size;
                load[read_p0()].exec(size);
                IP = IP + 16;
            },
            () -> { // 2 -> STORE
                int size = read_x0();
                store[read_p0()].exec(size);
                SP = SP + size;
                IP = IP + 16;
            },
            () -> { // 3 -> RET
                power = false;
                IP = IP + 16;
            },
            () -> { // 4 -> ADD
                add[read_p0()].exec();
                IP = IP + 16;
            },
    };
}
