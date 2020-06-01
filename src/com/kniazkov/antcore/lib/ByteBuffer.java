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
package com.kniazkov.antcore.lib;

/**
 * The buffer of bytes
 */
public class ByteBuffer extends ByteList {
    public ByteBuffer(int capacity) {
        data = new byte[capacity];
    }

    @Override
    public int size() {
        return data.length;
    }

    @Override
    public byte[] toArray() {
        return data.clone();
    }

    @Override
    public int copy(int fromIndex, byte[] destination, int toIndex, int size) {
        if (fromIndex >= data.length || toIndex >= data.length)
            return 0;
        if (fromIndex + size > data.length)
            size = data.length - fromIndex;
        if (toIndex + size > destination.length)
            size = destination.length - toIndex;
        if (size >= 0)
            System.arraycopy(data, fromIndex, destination, toIndex, size);
        return size;
    }

    @Override
    public byte get(int index) throws IndexOutOfBoundsException {
        return data[index];
    }

    public void set(int index, byte value) throws IndexOutOfBoundsException {
        data[index] = value;
    }

    @Override
    public char getChar(int index) throws IndexOutOfBoundsException {
        return (char)((data[index] & 0xFF) | ((data[index + 1] & 0xFF) << 8));
    }

    public void setChar(int index, char value) throws IndexOutOfBoundsException {
        data[index++] = (byte)(value);
        data[index] = (byte)(value >> 8);
    }

    @Override
    public short getShort(int index) throws IndexOutOfBoundsException {
        return (short)((data[index] & 0xFF) | ((data[index + 1] & 0xFF) << 8));
    }

    public void setShort(int index, short value) throws IndexOutOfBoundsException {
        data[index++] = (byte)(value);
        data[index] = (byte)(value >> 8);
    }

    @Override
    public int getInt(int index) throws IndexOutOfBoundsException {
        // it is a kind of magic
        return (data[index] & 0xFF) | ((data[index + 1] & 0xFF) << 8) |
                ((data[index + 2] & 0xFF) << 16) | ((data[index + 3] & 0xFF) << 24);
    }

    public void setInt(int index, int value) throws IndexOutOfBoundsException {
        data[index++] = (byte)(value);
        data[index++] = (byte)(value >> 8);
        data[index++] = (byte)(value >> 16);
        data[index]   = (byte)(value >> 24);
    }

    public void setByteList(int index, ByteList list) {
        list.copy(0, data, index, list.size());
    }

    public void setArray(int toIndex, byte[] source, int fromIndex, int size) {
        System.arraycopy(source, fromIndex, data, toIndex, size);
    }

    public void move(int fromIndex, int toIndex, int size) {
        System.arraycopy(data, fromIndex, data, toIndex, size);
    }

    private byte[] data;
}
