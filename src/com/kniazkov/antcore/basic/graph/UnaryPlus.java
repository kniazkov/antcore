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

import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.exceptions.OperatorNotApplicable;

/**
 * The node represents unary plus
 */
public class UnaryPlus extends UnaryOperation {
    public UnaryPlus(Expression expression) {
        super(expression);
    }

    @Override
    void checkType() throws SyntaxError {
        DataType type = expression.getType().getPureType();
        if (type instanceof ShortType || type instanceof IntegerType)
            return;
        throw new OperatorNotApplicable(getFragment(), "+", expression.getType().getName());
    }

    @Override
    protected String getOperator() {
        return "+";
    }

    @Override
    public Object calculate() {
        return expression.calculate();
    }

    @Override
    public void genLoad(CompilationUnit unit) throws SyntaxError {
        expression.genLoad(unit);
    }
}
