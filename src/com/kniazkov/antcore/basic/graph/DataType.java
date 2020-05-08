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
package com.kniazkov.antcore.basic.graph;


import com.kniazkov.antcore.basic.Fragment;

/**
 * The basic interface for data type
 */
public abstract class DataType extends Node {
    public DataType(Fragment fragment) {
        super(fragment);
    }

    /**
     * @return name of the type
     */
    public abstract String getName();

    /**
     * @return size of the type
     */
    public abstract int getSize();

    /**
     * @return true if the type is built-it
     */
    public abstract boolean builtIn();

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(getName());
    }
}
