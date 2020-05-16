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
import com.kniazkov.antcore.basic.bytecode.TypeSelector;
import com.kniazkov.antcore.basic.bytecodebuilder.Add;
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.exceptions.OperatorNotApplicable;

/**
 * The node represents addition, i.e. operation '+'
 */
public class Addition extends BinaryOperation {
    public Addition(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String getOperator() {
        return "+";
    }

    @Override
    void defineType() throws SyntaxError {
        DataType leftType = left.getType().getPureType();
        DataType rightType = right.getType().getPureType();

        if (leftType instanceof IntegerType && rightType instanceof IntegerType) {
            setType(leftType);
            return;
        }

        if (leftType instanceof StringType && rightType instanceof StringType) {
            StringType leftTypeString = (StringType) leftType;
            StringType rightTypeString = (StringType) rightType;
            int length = leftTypeString.getStringLength() + rightTypeString.getStringLength();
            setType(new StringType(length));
            return;
        }

        throw new OperatorNotApplicable(getFragment(), getOperator(), leftType.getName(), rightType.getName());
    }

    @Override
    public Object calculate() {
        Object leftValue = left.calculate();
        if (leftValue == null)
            return null;

        Object rightValue = right.calculate();
        if (rightValue == null)
            return null;

        if (leftValue instanceof String || rightValue instanceof String)
            return String.valueOf(leftValue) + String.valueOf(rightValue);

        if (leftValue instanceof Integer) {
            if (rightValue instanceof Integer) {
                return (Integer)leftValue + (Integer)rightValue;
            }
        }
        return null;
    }

    @Override
    public void load(CompilationUnit cu) throws SyntaxError {
        DataType leftType = left.getType().getPureType();
        DataType rightType = right.getType().getPureType();

        right.load(cu);
        left.load(cu);
        if (leftType instanceof IntegerType || rightType instanceof IntegerType) {
            cu.addInstruction(new Add(TypeSelector.INTEGER));
            return;
        }

        assert(false);
    }
}
