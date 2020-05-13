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

import com.kniazkov.antcore.basic.SyntaxError;
import com.kniazkov.antcore.basic.exceptions.StringLengthMustBeConstant;

/**
 * The STRING data type
 */
public class StringType extends DataType implements ExpressionOwner {
    public StringType(Expression length) {
        this.length = length;
        this.lengthCache = -1;
        length.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        length.dfs(visitor);
        accept(visitor);
    }

    @Override
    public String getName() {
        return "STRING OF " + length.calculate();
    }

    public int getStringLength() throws SyntaxError {
        if (lengthCache < 0) {
            Object value = length.calculate();
            if (value instanceof Short)
                lengthCache = ((Short) value).intValue();
            else if (value instanceof Integer)
                lengthCache = (Integer) value;
            else
                throw new StringLengthMustBeConstant(getFragment());
        }
        return lengthCache;
    }

    @Override
    public int getSize() throws SyntaxError {
        return (1 + getStringLength()) * 2;
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
    public boolean canBeCastTo(DataType type) throws SyntaxError {
        if (type instanceof StringType) {
            StringType otherStringType = (StringType) type;
            int thisLength = getStringLength();
            int otherLength = otherStringType.getStringLength();
            return otherLength >= thisLength;
        }
        return false;
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    private DataTypeOwner owner;
    private Expression length;
    private int lengthCache;
}
