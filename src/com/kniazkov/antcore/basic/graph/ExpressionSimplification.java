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
import com.kniazkov.antcore.lib.Variant;

/**
 * Expression simplification algorithm
 */
public class ExpressionSimplification {
    /**
     * Applies the algorithm to a node that contains expressions
     * @param node a node
     */
    public static void apply(ExpressionOwner node) throws SyntaxError {
        Expression[] list = node.getExpressions();
        if (list.length == 0)
            return;

        boolean changes = false;
        int k;
        for (k = 0; k < list.length; k++) {
            Expression expression = list[k];
            if (expression != null && !expression.isAtomic()) {
                Variant data = expression.calculate();
                if (!data.isNull()) {
                    DataType type = expression.getType();
                    Expression atomic = type.createExpression(data);
                    if (atomic != null) {
                        list[k] = atomic;
                        changes = true;
                    }
                }
            }
        }
        if (changes)
            node.replaceExpressions(list);
    }
}
