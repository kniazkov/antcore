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
import com.kniazkov.antcore.basic.bytecodebuilder.PushLocalPointer;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.lib.Variant;

/**
 * The node represents pointer to a temporary object
 */
public class TemporaryPointer extends Expression {
    public TemporaryPointer(Expression expression, Variable tmpVar) {
        this.expression = expression;
        this.tmpVar = tmpVar;
    }

    @Override
    protected Node[] getChildren() {
        if (type != null)
            return new Node[] { expression, type, tmpVar };
        return new Node[] { expression, tmpVar };
    }

    @Override
    public DataType getType() throws SyntaxError {
        if (type == null)
            type = new Pointer(expression.getType());
        return type;
    }

    @Override
    public Variant calculate() {
        return Variant.createNull();
    }

    @Override
    public void toDeclarationSourceCode(StringBuilder buff, String i) {
        expression.toDeclarationSourceCode(buff, i);
    }

    @Override
    public void toUsageSourceCode(StringBuilder buff) {
        expression.toUsageSourceCode(buff);
    }

    @Override
    public void genLoad(CompilationUnit unit) throws SyntaxError {
        expression.genLoad(unit);
        tmpVar.genStore(unit);
        unit.addInstruction(new PushLocalPointer(tmpVar.getOffset()));
    }

    private Expression expression;
    private Variable tmpVar;
    private DataType type;
}
