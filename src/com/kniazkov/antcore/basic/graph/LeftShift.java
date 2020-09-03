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
import com.kniazkov.antcore.basic.bytecodebuilder.Shl;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.exceptions.OperatorNotApplicable;
import com.kniazkov.antcore.lib.Variant;

/**
 * The node represents left shift operation
 */
public class LeftShift extends BinaryOperation {
    public LeftShift(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String getOperator() {
        return "SHL";
    }

    @Override
    public Variant calculate() {
        return left.calculate().leftShift(right.calculate());
    }

    @Override
    public void genLoad(CompilationUnit unit) throws SyntaxError {
        DataType leftType = getLeftPureNonConstantType();
        DataType rightType = getRightPureNonConstantType();
        assert(leftType == rightType);

        right.genLoad(unit);
        left.genLoad(unit);

        int size = leftType.getSize();
        unit.addInstruction(new Shl(leftType.getSelector(), size, size, size));
    }
}
