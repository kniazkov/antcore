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
 * The list of bytes
 */
public interface ByteList {
    /**
     * @return size of the list
     */
    int size();

    /**
     * @param index the index
     * @return an item by index or 0 if index is incorrect
     */
    byte get(int index);

    /**
     * @return a copy of the list represented as an array
     */
    byte[] toArray();

    /**
     * Copy data to byte array
     * @param fromIndex from what position of this list starting copying
     * @param size the size of data
     * @param destination the destination array
     * @param toIndex the starting position in the destination array
     * @return number of bytes copied
     */
    int copy(int fromIndex, int size, byte[] destination, int toIndex);
}
