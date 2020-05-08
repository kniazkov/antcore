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
 * The node represents a field (of class, type, etc)
 */
public class Field extends Expression implements DataTypeOwner {
    public Field(Fragment fragment, String name, String typeName, DataType type) {
        this.fragment = fragment;
        this.name = name;
        this.typeName = typeName;
        this.type = type;
        if (type != null)
            type.setOwner(this);
    }

    public String getName() {
        return name;
    }

    void setOwner(DataSet owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return owner;
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public DataType getType() {
        return type;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append(name).append(" AS ");
        if (type != null)
            type.toSourceCode(buff, i, i0);
        else
            buff.append(typeName);
        buff.append("\n");
    }

    private DataSet owner;
    private Fragment fragment;
    private String name;
    private String typeName;
    private DataType type;
    private int offset;
}
