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

import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.exceptions.UnknownType;

/**
 * The reference to data type
 */
public class DataTypeReference extends DataType {
    public DataTypeReference(String name, DataType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    void setOwner(DataTypeOwner owner) {
        this.owner = owner;
    }

    @Override
    public String getName() {
        if (type != null)
            return type.getName();
        return name;
    }

    @Override
    public int getSize() throws SyntaxError {
        return type.getSize();
    }

    @Override
    public boolean builtIn() {
        return type.builtIn();
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    @Override
    public DataType getPureType() {
        return type;
    }

    /**
     * Bind data type by name
     */
    void bindType() throws SyntaxError {
        if (type != null)
            return;
        type = findTypeByName(name);
        if (type == null)
            throw new UnknownType(getFragment(), name);
    }

    @Override
    public boolean canBeCastTo(DataType otherType) throws SyntaxError {
        return type.canBeCastTo(otherType);
    }

    private DataTypeOwner owner;
    private String name;
    private DataType type;
}
