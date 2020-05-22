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
 * The STRING data type without size specified
 */
public class AbstractStringType extends BuiltInType implements ExpressionOwner {
    @Override
    public String getName() {
        return "STRING";
    }

    @Override
    public int getSize() throws SyntaxError {
        return 0;
    }

    @Override
    public byte getSelector() {
        return TypeSelector.STRING;
    }

    @Override
    public boolean isAbstract() {
        return true;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public boolean isBinaryAnalog(DataType otherType) throws SyntaxError {
        return false;
    }

    @Override
    public boolean isInheritedFrom(DataType otherType) {
        return false;
    }

    @Override
    public Expression staticCast(Expression expression, DataType otherType) throws SyntaxError {
        return null;
    }

    @Override
    public Expression dynamicCast(Expression expression, DataType otherType) {
        return null;
    }

    private AbstractStringType() {
    }

    private static AbstractStringType instance;

    public static DataType getInstance() {
        if (instance == null)
            instance = new AbstractStringType();
        return instance;
    }
}
