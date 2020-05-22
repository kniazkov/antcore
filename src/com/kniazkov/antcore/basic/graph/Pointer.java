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

import com.kniazkov.antcore.basic.bytecode.TypeSelector;
import com.kniazkov.antcore.basic.common.SyntaxError;

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
    public byte getSelector() {
        return TypeSelector.POINTER;
    }

    @Override
    public boolean isBuiltIn() {
        return true;
    }

    @Override
    public boolean isAbstract() {
        return false;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    void setOwner(DataTypeOwner owner) {
        this.owner = owner;
    }

    @Override
    public boolean isBinaryAnalog(DataType otherType) throws SyntaxError {
        if (otherType instanceof Pointer) {
            Pointer otherPointerType = (Pointer)otherType;
            return this.type.isBinaryAnalog(otherPointerType.type);
        }
        return false;
    }

    @Override
    public boolean isInheritedFrom(DataType otherType) {
        return false;
    }

    @Override
    public Expression staticCast(Expression expression, DataType otherType) {
        return null;
    }

    @Override
    public Expression dynamicCast(Expression expression, DataType otherType) throws SyntaxError {
        if (otherType instanceof Pointer) {
            if (type.isBinaryAnalog(((Pointer) otherType).type))
                return expression;
        }
        if (type.isBinaryAnalog(otherType) || otherType.isInheritedFrom(type))
            return expression.getPointer();
        return null;
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    private DataTypeOwner owner;
    private DataType type;
}
