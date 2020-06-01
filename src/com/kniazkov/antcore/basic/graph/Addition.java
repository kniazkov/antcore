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

import com.kniazkov.antcore.basic.bytecodebuilder.Cast;
import com.kniazkov.antcore.basic.common.SyntaxError;
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

        if (leftType instanceof StringType) {
            StringType leftTypeString = (StringType) leftType;
            if (rightType instanceof StringType) {
                StringType rightTypeString = (StringType) rightType;
                int length = leftTypeString.getStringLength() + rightTypeString.getStringLength();
                setType(new StringType(length));
                return;
            }

            if (rightType instanceof ShortType) {
                Casting cast = new Casting(true, right, new StringType(lengthOfStringContainingShort));
                cast.setOwner(this);
                right = cast;
                int length = leftTypeString.getStringLength() + lengthOfStringContainingShort;
                setType(new StringType(length));
                return;
            }

            if (rightType instanceof IntegerType) {
                Casting cast = new Casting(true, right, new StringType(lengthOfStringContainingInteger));
                cast.setOwner(this);
                right = cast;
                int length = leftTypeString.getStringLength() + lengthOfStringContainingInteger;
                setType(new StringType(length));
                return;
            }
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
    public void genLoad(CompilationUnit cu) throws SyntaxError {
        DataType leftType = left.getType().getPureType();
        DataType rightType = right.getType().getPureType();

        right.genLoad(cu);
        left.genLoad(cu);
        if (leftType instanceof IntegerType && rightType instanceof IntegerType) {
            cu.addInstruction(new Add(TypeSelector.INTEGER,4, 4, 4));
            return;
        }

        if (leftType instanceof StringType && rightType instanceof StringType) {
            cu.addInstruction(new Add(TypeSelector.STRING,
                    leftType.getSize(), rightType.getSize(), getType().getSize()));
            return;
        }

        assert(false);
    }

    protected final static int lengthOfStringContainingShort = 6; // -32768
    protected final static int lengthOfStringContainingInteger = 11; // -2147483648
}
