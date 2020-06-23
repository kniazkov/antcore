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

import com.kniazkov.antcore.basic.bytecode.ComparatorSelector;
import com.kniazkov.antcore.basic.bytecodebuilder.Compare;
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.common.SyntaxError;

/**
 * The node represents 'less' operation, i.e. operation '<'
 */
public class Less extends BinaryOperation {
    public Less(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String getOperator() {
        return "<";
    }

    @Override
    void defineType() throws SyntaxError {
        setType(BooleanType.getInstance());
    }

    @Override
    public Object calculate() {
       return false;
    }

    @Override
    public void genLoad(CompilationUnit unit) throws SyntaxError {
        DataType leftType = left.getType().getPureType();
        DataType rightType = right.getType().getPureType();

        right.genLoad(unit);
        left.genLoad(unit);

        unit.addInstruction(new Compare(leftType.getSelector(), ComparatorSelector.LESS,
                leftType.getSize(), rightType.getSize()));
    }
}
