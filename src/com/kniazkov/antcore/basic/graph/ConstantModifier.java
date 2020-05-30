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

/**
 * The constant modifier for data type
 */
public class ConstantModifier extends DataType implements DataTypeOwner {
    public ConstantModifier(DataType type) {
        this.type = type;
        type.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        return new Node[]{ type };
    }

    @Override
    public String getName() {
        return "CONST " + type.getName();
    }

    @Override
    public int getSize() throws SyntaxError {
        return type.getSize();
    }

    @Override
    public byte getSelector() {
        return type.getSelector();
    }

    @Override
    public boolean isBuiltIn() {
        return true;
    }

    @Override
    public boolean isAbstract() {
        return type.isAbstract();
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    void setOwner(DataTypeOwner owner) {
        this.owner = owner;
    }

    @Override
    public DataType getPureType() {
        return type;
    }

    @Override
    public boolean isBinaryAnalog(DataType otherType) throws SyntaxError {
        return type.isBinaryAnalog(otherType);
    }

    @Override
    public boolean isInheritedFrom(DataType otherType) {
        return type.isInheritedFrom(otherType);
    }

    @Override
    public Expression staticCast(Expression expression, DataType otherType) throws SyntaxError {
        return type.staticCast(expression, otherType);
    }

    @Override
    public Expression dynamicCast(Expression expression, DataType otherType) throws SyntaxError {
        return type.dynamicCast(expression, otherType);
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    private DataTypeOwner owner;
    private DataType type;
}
