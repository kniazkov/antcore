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
        length.setOwner(this);
    }

    @Override
    public String getName() {
        return "STRING OF " + length.calculate();
    }

    @Override
    public int getSize() throws SyntaxError {
        Object value = length.calculate();
        if (value instanceof Short)
            return 1 + ((Short) value).intValue();
        if (value instanceof Integer)
            return 1 + (Integer) value;
        throw new StringLengthMustBeConstant(getFragment());
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

    private DataTypeOwner owner;
    private Expression length;
}
