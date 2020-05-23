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
 * A list of bytes
 */
public abstract class ByteList {
    /**
     * @return size of the list
     */
    public abstract int size();

    /**
     * @param index the index
     * @return an item by index
     */
    public abstract byte get(int index);

    /**
     * @return a copy of the list represented as an array
     */
    public abstract byte[] toArray();

    /**
     * Copy data to byte array
     * @param fromIndex from what position of this list starting copying
     * @param size the size of data
     * @param destination the destination array
     * @param toIndex the starting position in the destination array
     * @return number of bytes copied
     */
    public abstract int copy(int fromIndex, byte[] destination, int toIndex, int size);

    /**
     * Represents a byte array as ByteList
     * @param array an array
     * @return a ByteList object
     */
    public static ByteList wrap(byte[] array) {
        return new ArrayWrapper(array);
    }

    /**
     * @param index the index
     * @return char (2 bytes) by index
     */
    public char getChar(int index) throws IndexOutOfBoundsException {
        return (char)(((int)get(index) | ((int)get(index + 1) << 8)));
    }

    /**
     * @param index the index
     * @return integer (4 bytes) by index
     */
    public int getInt(int index) throws IndexOutOfBoundsException {
        return ((int)get(index)) | ((int)get(index + 1) << 8) |
                ((int)get(index + 2) << 16) | ((int)get(index + 3) << 24);
    }

    private static class ArrayWrapper extends ByteList {
        public ArrayWrapper(byte[] array) {
            this.array = array;
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public byte get(int index) throws IndexOutOfBoundsException {
            return array[index];
        }

        @Override
        public byte[] toArray() {
            return array.clone();
        }

        @Override
        public int copy(int fromIndex, byte[] destination, int toIndex, int size) {
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
}
