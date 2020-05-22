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
package com.kniazkov.antcore.basic.parser;

import com.kniazkov.antcore.basic.graph.AbstractStringType;
import com.kniazkov.antcore.basic.graph.DataType;
import com.kniazkov.antcore.basic.graph.Pointer;
import com.kniazkov.antcore.basic.graph.StringType;
import com.kniazkov.antcore.basic.parser.tokens.TokenExpression;

/**
 * A data type represented as pointer to another type
 */
public class RawDataTypeString extends RawDataType {
    public RawDataTypeString() {
        this.length = null;
    }

    public RawDataTypeString(TokenExpression length) {
        this.length = length;
    }

    @Override
    public String getName() {
        if (length != null)
            return "STRING OF " + length.toString();
        return "STRING";
    }

    @Override
    public DataType toNode() {
        if (length != null)
            return new StringType(length.toNode());
        return AbstractStringType.getInstance();
    }

    private TokenExpression length;
}
