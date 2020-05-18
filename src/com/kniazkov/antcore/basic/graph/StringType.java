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
import com.kniazkov.antcore.basic.exceptions.StringLengthMustBeConstant;

/**
 * The STRING data type
 */
public class StringType extends DataType implements ExpressionOwner {
    public StringType(Expression length) {
        this.lengthNode = length;
        length.setOwner(this);
        this.lengthValue = -1;
    }

    public StringType(int length) {
        this.lengthValue = length;
    }

    public StringType() {
        this.lengthValue = -1;
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        if (lengthNode != null)
            lengthNode.dfs(visitor);
        accept(visitor);
    }

    @Override
    public String getName() {
        try {
            int length = getStringLength();
            if (length >= 0)
                return "STRING OF " + length;
            return "STRING";
        } catch (SyntaxError syntaxError) {
            return "STRING OF ?";
        }
    }

    public int getStringLength() throws SyntaxError {
        if (lengthValue < 0 && lengthNode != null) {
            Object value = lengthNode.calculate();
            if (value instanceof Short)
                lengthValue = ((Short) value).intValue();
            else if (value instanceof Integer)
                lengthValue = (Integer) value;
            else
                throw new StringLengthMustBeConstant(getFragment());
        }
        return lengthValue;
    }

    @Override
    public int getSize() throws SyntaxError {
        int length = getStringLength();
        return length >= 0 ? (1 + length) * 2 : 0;
    }

    @Override
    public boolean isBuiltIn() {
        return true;
    }

    @Override
    public boolean isAbstract() {
        return lengthValue < 0 && lengthNode == null;
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
    private Expression lengthNode;
    private int lengthValue;
}
