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
import com.kniazkov.antcore.lib.Variant;

/**
 * The BYTE data type
 */
public class ByteType extends BuiltInType {
    @Override
    protected Node[] getChildren() {
        return null;
    }

    @Override
    public String getName() {
        return "BYTE";
    }

    @Override
    public int getSize() throws SyntaxError {
        return 1;
    }

    @Override
    public byte getSelector() {
        return TypeSelector.BYTE;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public boolean isNumeric() {
        return true;
    }

    @Override
    public boolean containsPointer() {
        return false;
    }

    @Override
    public boolean isBinaryAnalog(DataType otherType) throws SyntaxError {
        return otherType == this;
    }

    @Override
    public boolean isInheritedFrom(DataType otherType) {
        return false;
    }

    @Override
    public Expression staticCast(Expression expression, DataType otherType) throws SyntaxError {
        if (otherType == this)
            return expression;
        return null;
    }

    @Override
    public Expression dynamicCast(Expression expression, DataType otherType) throws SyntaxError {
        if (otherType == this)
            return expression;
        return null;
    }

    @Override
    public Expression createExpression(Variant var) {
        if (var.hasByteValue())
            return new ByteNode(var.byteValue());
        return null;
    }

    private ByteType() {
    }

    private static ByteType instance;

    public static DataType getInstance() {
        if (instance == null)
            instance = new ByteType();
        return instance;
    }
}
