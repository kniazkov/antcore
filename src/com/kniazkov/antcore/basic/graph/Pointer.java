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
 * The pointer data type
 */
public class Pointer extends DataType implements DataTypeOwner {
    public Pointer(String typeName, DataType type) {
        this.typeName = typeName;
        this.type = type;
        if (type != null)
            type.setOwner(this);
    }

    @Override
    public String getName() {
        return "POINTER TO " + (type != null ? type.getName() : typeName);
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public boolean builtIn() {
        return false;
    }

    @Override
    void setOwner(DataTypeOwner owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    private DataTypeOwner owner;
    private String typeName;
    private DataType type;
}
