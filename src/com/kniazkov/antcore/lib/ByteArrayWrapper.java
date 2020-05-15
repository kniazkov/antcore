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
 * The wrapper of byte array that implements byte list
 */
public class ByteArrayWrapper implements ByteList {
    public ByteArrayWrapper(byte[] array) {
        this.array = array;
    }

    @Override
    public int size() {
        return array.length;
    }

    @Override
    public byte get(int index) throws IndexOutOfBoundsException {
        return index >= 0 && index <= array.length ? array[index] : 0;
    }

    @Override
    public byte[] toArray() {
        return array.clone();
    }

    @Override
    public int copy(int fromIndex, int size, byte[] destination, int toIndex) {
        if (fromIndex >= array.length || toIndex >= destination.length)
            return 0;
        if (fromIndex + size > array.length)
            size = array.length - fromIndex;
        if (toIndex + size > destination.length)
            size = destination.length - toIndex;
        if (size >= 0)
            System.arraycopy(array, fromIndex, destination, toIndex, size);
        return size;
    }

    private byte[] array;
}
