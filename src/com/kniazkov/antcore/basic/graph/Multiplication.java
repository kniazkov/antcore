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
import com.kniazkov.antcore.lib.Variant;

/**
 * The node represents multiplication, i.e. operation '*'
 */
public class Multiplication extends BinaryOperation {
    public Multiplication(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    void defineType() throws SyntaxError {
        DataType leftType = getLeftPureNonConstantType();
        DataType rightType = getRightPureNonConstantType();

        if (leftType instanceof ShortType && rightType instanceof ShortType) {
            setType(IntegerType.getInstance());
            return;
        }

        if (leftType instanceof IntegerType) {
            if (rightType instanceof ShortType) {
                setType(leftType);
                right = new Casting(true, right, leftType);
                right.setOwner(this);
                return;
            }
            if (rightType instanceof IntegerType) {
                setType(leftType);
                return;
            }
        }

        if (leftType instanceof RealType && rightType instanceof RealType) {
            setType(leftType);
            return;
        }

        throw new OperatorNotApplicable(getFragment(), getOperator(),
                leftType.getName(), rightType.getName());
    }

    @Override
    protected String getOperator() {
        return "*";
    }

    @Override
    public Variant calculate() {
        return left.calculate().mul(right.calculate());
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
