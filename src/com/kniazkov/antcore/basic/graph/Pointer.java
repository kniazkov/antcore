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
import com.kniazkov.antcore.basic.SyntaxError;

/**
 * The pointer data type
 */
public class Pointer extends DataType implements DataTypeOwner {
    public Pointer(DataType type) {
        this.type = type;
        type.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        type.dfs(visitor);
        accept(visitor);
    }

    @Override
    public String getName() {
        return "POINTER TO " + type.getName();
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

    @Override
    public boolean canBeCastTo(DataType otherType) throws SyntaxError {
        if (otherType instanceof Pointer) {
            Pointer otherTypePointer = (Pointer) otherType;
            return type.canBeCastTo(otherTypePointer.type);
        }
        return false;
    }

    private DataTypeOwner owner;
    private DataType type;
}
