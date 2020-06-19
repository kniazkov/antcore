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
import com.kniazkov.antcore.basic.exceptions.IncompatibleTypes;

/**
 * Type casting methods
 */
public class DataTypeCast {
    /**
     * Casting an expression to expected type
     * @param expectedType expected type
     * @param expression an expression
     * @return an expression itself if no casting needed, or null if casting is impossible, or casted expression
     */
    public static Expression cast(DataType expectedType, Expression expression) throws SyntaxError {
        expectedType = expectedType.getPureType();
        DataType actualType = expression.getType().getPureType();

        assert (!(expectedType instanceof ConstantModifier));

        if (actualType instanceof ConstantModifier) {
            // we can overwrite non-constant field by constant value
            actualType = ((ConstantModifier) actualType).getNonConstantType();
        }

        if (expectedType instanceof Pointer) {
            expectedType = ((Pointer) expectedType).getType();
            if (actualType instanceof Pointer) {
                actualType = ((Pointer) actualType).getType();
                if (actualType instanceof ConstantModifier) {
                    if (!(expectedType instanceof ConstantModifier)) {
                        // we can not overwrite non-constant pointer by constant pointer (data safety)
                        return null;
                    }
                    expectedType = ((ConstantModifier) expectedType).getNonConstantType();
                    actualType = ((ConstantModifier) actualType).getNonConstantType();
                }
                if (expectedType.isBinaryAnalog(actualType) || actualType.isInheritedFrom(expectedType.getPureType()))
                    return expression;
                return null;
            }
            else {
                if (expectedType instanceof ConstantModifier)
                    expectedType = ((ConstantModifier) expectedType).getNonConstantType();
                if (expectedType.isBinaryAnalog(actualType) || actualType.isInheritedFrom(expectedType.getPureType()))
                    return expression.getPointer();
                return null;
            }
        }

        if (expectedType.isBinaryAnalog(actualType)) {
            return expression;
        }

        if (!(expectedType instanceof StringType)) {
            Expression sc = expectedType.staticCast(expression, actualType);
            if (sc != null)
                return sc;
        }

        return expectedType.dynamicCast(expression, actualType);
    }
}
