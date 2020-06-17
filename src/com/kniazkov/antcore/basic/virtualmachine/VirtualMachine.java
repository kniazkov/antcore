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

import com.kniazkov.antcore.lib.ByteBuffer;
import com.kniazkov.antcore.lib.ByteList;

import java.util.Map;

/**
 * The virtual machine that runs compiled code
 */
public class VirtualMachine {
    public VirtualMachine(ByteList code, int memorySize, Map<String, NativeFunction> functions) {
        memory = new ByteBuffer(memorySize);
        memory.setByteList(0, code);
        this.functions = functions;
    }

    /**
     * Starts execution
     */
    public void run() {
        IP = 0;
        SP = memory.size();
        LP = SP;
        power = true;
        error = ErrorCode.OK;
        while(power) {
            units[readOpcode()].exec();
        }
    }

    public ErrorCode getErrorCode() {
        return error;
    }

    public int getInstructionPointer() {
        return IP;
    }

    ByteBuffer memory;
    Map<String, NativeFunction> functions;
    boolean power;
    ErrorCode error;
    int IP;             // instruction pointer
    int SP;             // stack pointer
    int LP;             // local pointer

    final byte readOpcode() {
        return memory.get(IP);
    }

    final byte read_p0() {
        return memory.get(IP + 1);
    }

    final byte read_p1() {
        return memory.get(IP + 2);
    }

    final byte read_p2() {
        return memory.get(IP + 3);
    }

    final int read_x0() {
        return memory.getInt(IP + 4);
    }

    final int read_x1() {
        return memory.getInt(IP + 8);
    }

    final int read_x2() {
        return memory.getInt(IP + 12);
    }

    final void move(int fromPos, int toPos, int size) {
        memory.move(fromPos, toPos, size);
    }

    final void pushBoolean(boolean value) {
        SP = SP - 1;
        memory.set(SP, (byte) (value ? 1 : 0));
    }

    final boolean popBoolean() {
        boolean value = memory.get(SP) != 0;
        SP = SP + 1;
        return value;
    }

    final void pushShort(short value) {
        SP = SP - 2;
        memory.setShort(SP, value);
    }

    final short popShort() {
        short value = memory.getShort(SP);
        SP = SP + 2;
        return value;
    }

    final void pushInteger(int value) {
        SP = SP - 4;
        memory.setInt(SP, value);
    }

    final int popInteger() {
        int value = memory.getInt(SP);
        SP = SP + 4;
        return value;
    }

    interface Unit {
        void exec();
    }

    final Unit stub = () -> {
        power = false;
        error = ErrorCode.BAD_INSTRUCTION;
    };
    
    interface Load {
        void exec(int size);
    }

    final Load[] load = {
            (size) -> { // 0 -> GLOBAL
                move(read_x1(), SP, size);
            },
            (size) -> { // 1 -> LOCAL
                move(LP + read_x1(), SP, size);
            },
            (size) -> { // 2 -> IMMEDIATE
                move(IP + 8, SP, size);
            },
            (size) -> { // 3 -> LOCAL_POINTER
                assert (size == 4);
                memory.setInt(SP, LP + read_x1());
            },
            (size) -> { // 4 -> ZERO
                for (int k = 0; k < size; k++)
                    memory.set(SP + k, (byte) 0);
            }
    };

    interface Store {
        void exec(int size);
    }

    final Store[] store = {
            (size) -> { // 0 -> GLOBAL
                move(SP, read_x1(), size);
            },
            (size) -> { // 1 -> LOCAL
                move(SP, LP + read_x1(), size);
            },
    };

    final Unit[] castToShort = {
            stub,
            stub,   // 1 -> POINTER
            stub,   // 2 -> BOOLEAN
            stub,   // 3 -> BYTE
            stub,   // 4 -> SHORT
            () -> { // 5 -> INTEGER
                int value = popInteger();
                if (value > Short.MAX_VALUE) pushShort(Short.MAX_VALUE);
                else if (value < Short.MIN_VALUE) pushShort(Short.MIN_VALUE);
                else pushShort((short)value);
            },
            stub,   // 6 -> LONG
            stub,   // 7 -> REAL
            stub,   // 8 -> STRING
            stub,   // 9 -> ARRAY
            stub    // 10 -> STRUCT
    };

    final Unit[] castToInteger = {
            stub,
            stub,   // 1 -> POINTER
            stub,   // 2 -> BOOLEAN
            stub,   // 3 -> BYTE
            stub,   // 4 -> SHORT
            stub,   // 5 -> INTEGER
            stub,   // 6 -> LONG
            stub,   // 7 -> REAL
            stub,   // 8 -> STRING
            stub,   // 9 -> ARRAY
            stub    // 10 -> STRUCT
    };

    void castAnyToString(String strValue, int currentSize) {
        int newSize = read_x1();
        int capacity = (newSize - 8) / 2;
        int strValueLength = strValue.length();
        assert (capacity >= strValueLength);
        SP -= newSize - currentSize;
        memory.setInt(SP, strValueLength);
        memory.setInt(SP + 4, capacity);
        for (int k = 0; k < strValueLength; k++) {
            char ch = strValue.charAt(k);
            memory.setChar(SP + 8 + k * 2, ch);
        }
    }

    final Unit[] castToString = {
            stub,
            stub,   // 1 -> POINTER
            () -> { // 2 -> BOOLEAN
                assert(read_x0() == 1);
                castAnyToString(memory.get(SP) == 0 ? "FALSE" : "TRUE", 1);
            },
            stub,   // 3 -> BYTE
            () -> { // 4 -> SHORT
                assert(read_x0() == 2);
                castAnyToString(String.valueOf(memory.getShort(SP)), 2);
            },
            () -> { // 5 -> INTEGER
                assert(read_x0() == 4);
                castAnyToString(String.valueOf(memory.getInt(SP)), 4);
            },
            stub,   // 6 -> LONG
            stub,   // 7 -> REAL
            () -> { // 8 -> STRING
                int currSize = read_x0();
                int newSize = read_x1();
                if (newSize > currSize) {
                    int diff = newSize - currSize;
                    assert(diff % 2 == 0);
                    StringData string = StringData.read(memory, SP);
                    SP = SP - diff;
                    string.capacity += diff / 2;
                    string.write(memory, SP);
                }
                else if (currSize > newSize) {
                    int diff = currSize - newSize;
                    assert(diff % 2 == 0);
                    StringData string = StringData.read(memory, SP);
                    SP = SP + diff;
                    string.capacity -= diff / 2;
                    if (string.length > string.capacity)
                        string.length = string.capacity;
                    string.write(memory, SP);
                }
            },
            stub,   // 9 -> ARRAY
            stub    // 10 -> STRUCT
    };

    final Unit[] cast = {
            stub,
            stub,   // 1 -> POINTER
            stub,   // 2 -> BOOLEAN
            stub,   // 3 -> BYTE
            () -> { // 4 -> SHORT
                castToShort[read_p0()].exec();
            },
            () -> { // 5 -> INTEGER
                castToInteger[read_p0()].exec();
            },
            stub,   // 6 -> LONG
            stub,   // 7 -> REAL
            () -> { // 8 -> STRING
                castToString[read_p0()].exec();
            },
            stub,   // 9 -> ARRAY
            stub    // 10 -> STRUCT
    };

    final Unit[] call = {
            () -> { // 0 -> NATIVE
                StringData functionName = StringData.read(memory, read_x0());
                NativeFunction function = functions.get(functionName.toString());
                if (function == null) {
                    power = false;
                    error = ErrorCode.FUNCTION_NOT_DEFINED;
                    return;
                }
                function.exec(memory, SP);
                IP = popInteger();
            },
            () -> { // 1 -> USER_DEFINED
                IP = read_x0();
            }
    };

    final Unit[] add = {
            stub,
            stub,   // 1 -> POINTER
            stub,   // 2 -> BOOLEAN
            stub,   // 3 -> BYTE
            stub,   // 4 -> SHORT
            () -> { // 5 -> INTEGER
                pushInteger(popInteger() + popInteger());
            },
            stub,   // 6 -> LONG
            stub,   // 7 -> REAL
            () -> { // 8 -> STRING
                StringData str1 = StringData.read(memory, SP);
                SP = SP + read_x0();
                StringData str2 = StringData.read(memory, SP);
                SP = SP + read_x1();
                StringData result = new StringData();
                int resultLength = read_x2();
                result.capacity = (resultLength - 8) / 2;
                StringData.concat(result, str1, str2);
                SP = SP - resultLength;
                result.write(memory, SP);
            },
            stub,   // 9 -> ARRAY
            stub    // 10 -> STRUCT
    };

    final Unit[] sub = {
            stub,
            stub,   // 1 -> POINTER
            stub,   // 2 -> BOOLEAN
            stub,   // 3 -> BYTE
            stub,   // 4 -> SHORT
            () -> { // 5 -> INTEGER
                int left = popInteger();
                int right = popInteger();
                pushInteger(left - right);
            },
            stub,   // 6 -> LONG
            stub,   // 7 -> REAL
            stub,   // 8 -> STRING
            stub,   // 9 -> ARRAY
            stub    // 10 -> STRUCT
    };

    final Unit[] compareInteger = {
            () -> { // 0 -> EQUAL
                int left = popInteger();
                int right = popInteger();
                pushBoolean(left == right);
            },
            () -> { // 1 -> DIFF
                int left = popInteger();
                int right = popInteger();
                pushBoolean(left != right);
            },
            () -> { // 2 -> LESS
                int left = popInteger();
                int right = popInteger();
                pushBoolean(left < right);
            },
            () -> { // 3 -> LESS_EQUAL
                int left = popInteger();
                int right = popInteger();
                pushBoolean(left <= right);
            },
            () -> { // 4 -> GREATER
                int left = popInteger();
                int right = popInteger();
                pushBoolean(left > right);
            },
            () -> { // 4 -> GREATER_EQUAL
                int left = popInteger();
                int right = popInteger();
                pushBoolean(left >= right);
            }
    };

    final Unit[] compare = {
            stub,
            stub,   // 1 -> POINTER
            stub,   // 2 -> BOOLEAN
            stub,   // 3 -> BYTE
            stub,   // 4 -> SHORT
            () -> { // 5 -> INTEGER
                compareInteger[read_p1()].exec();
            },
            stub,   // 6 -> LONG
            stub,   // 7 -> REAL
            stub,   // 8 -> STRING
            stub,   // 9 -> ARRAY
            stub    // 10 -> STRUCT
    };

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
            () -> { // 3 -> CAST
                cast[read_p1()].exec();
                IP = IP + 16;
            },
            () -> { // 4 -> POP
                SP = SP + read_x0();
                IP = IP + 16;
            },
            () -> { // 5 -> CALL
                pushInteger(IP + 16);
                call[read_p0()].exec();
            },
            () -> { // 6 -> RET
                if (SP == memory.size())
                    power = false;
                else
                    IP = popInteger();
            },
            () -> { // 7 -> ENTER
                pushInteger(LP);
                LP = SP;
                SP = SP - read_x0();
                IP = IP + 16;
            },
            () -> { // 8 -> LEAVE
                SP = SP + read_x0();
                LP = popInteger();
                IP = IP + 16;
            },
            () -> { // 9 -> ADD
                add[read_p0()].exec();
                IP = IP + 16;
            },
            () -> { // 10 -> SUB
                sub[read_p0()].exec();
                IP = IP + 16;
            },
            () -> { // 11 -> CMP
                compare[read_p0()].exec();
                IP = IP + 16;
            },
            () -> { // 12 -> IF
                boolean value = popBoolean();
                boolean condition = read_p0() > 0;
                if (value == condition)
                    IP = read_x0();
                else
                    IP = IP + 16;
            },
            () -> { // 13 -> JUMP
                IP = read_x0();
            },
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
            () -> { // 127 -> END
                power = false;
            }
    };
}
