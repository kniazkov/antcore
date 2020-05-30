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

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        return lengthNode != null ? new Node[] { lengthNode } : null;
    }

    @Override
    public String getName() {
        try {
            int length = getStringLength();
            return "STRING OF " + length;
        } catch (SyntaxError syntaxError) {
            return "STRING OF ?";
        }
    }

    public int getStringLength() throws SyntaxError {
        if (lengthValue < 0) {
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
        return length * 2 + 8;
    }

    @Override
    public byte getSelector() {
        return TypeSelector.STRING;
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
        if (otherType instanceof StringType) {
            StringType otherTypeString = (StringType)otherType;
            return this.getStringLength() == otherTypeString.getStringLength();
        }
        return false;
    }

    @Override
    public boolean isInheritedFrom(DataType otherType) {
        return otherType == AbstractStringType.getInstance();
    }

    @Override
    public Expression staticCast(Expression expression, DataType otherType) throws SyntaxError {
        if (otherType instanceof StringType) {
            StringType otherTypeString = (StringType)otherType;
            int thisLength = this.getStringLength();
            int otherLength = otherTypeString.getStringLength();
            if (thisLength >= otherLength)
                return expression;
            String data = (String) expression.calculate();
            if (data == null)
                return null;
            return new StringNode(data.substring(0, thisLength));
        }
        return null;
    }

    @Override
    public Expression dynamicCast(Expression expression, DataType otherType) {
        if (otherType instanceof StringType)
            return new Casting(true, expression, this);
        return null;
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    private DataTypeOwner owner;
    private Expression lengthNode;
    private int lengthValue;
}
