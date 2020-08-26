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
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.bytecodebuilder.Mul;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.exceptions.OperatorNotApplicable;

/**
 * The node represents multiplication, i.e. operation '*'
 */
public class Multiplication extends BinaryOperation {
    public Multiplication(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String getOperator() {
        return "*";
    }

    @Override
    void defineType() throws SyntaxError {
        DataType leftType = getLeftPureNonConstantType();
        DataType rightType = getRightPureNonConstantType();

        if (leftType instanceof ShortType && rightType instanceof ShortType) {
            setType(IntegerType.getInstance());
            return;
        }

        if (leftType instanceof IntegerType && rightType instanceof IntegerType) {
            setType(leftType);
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

        if (leftValue instanceof Short) {
            if (rightValue instanceof Short) {
                return (short)((Short)leftValue * (Short)rightValue);
            }
        }

        if (leftValue instanceof Integer) {
            if (rightValue instanceof Integer) {
                return (Integer)leftValue * (Integer)rightValue;
            }
        }

        return null;
    }

    @Override
    public void genLoad(CompilationUnit unit) throws SyntaxError {
        DataType leftType = getLeftPureNonConstantType();
        DataType rightType = getRightPureNonConstantType();

        right.genLoad(unit);
        left.genLoad(unit);

        if (leftType instanceof ShortType && rightType instanceof ShortType) {
            unit.addInstruction(new Mul(TypeSelector.SHORT,2, 2, 4));
            return;
        }

        if (leftType instanceof IntegerType && rightType instanceof IntegerType) {
            unit.addInstruction(new Mul(TypeSelector.INTEGER,4, 4, 4));
            return;
        }

        assert(false);
    }
}
