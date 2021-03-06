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
import com.kniazkov.antcore.basic.bytecodebuilder.Pop;
import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.lib.Variant;

/**
 * The statement that has only one expression
 */
public class StatementExpression extends Statement implements ExpressionOwner {
    public StatementExpression(Fragment fragment, Expression expression) {
        super(fragment);
        this.expression = expression;
        expression.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        return new Node[]{ expression };
    }

    @Override
    public void compile(CompilationUnit unit) throws SyntaxError {
        Variant value = expression.calculate();
        if (value.isNull()) {
            expression.genLoad(unit);
            DataType returnType = expression.getType();
            if (returnType != null)
                unit.addInstruction(new Pop(returnType.getSize()));
        }
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i);
        expression.toUsageSourceCode(buff);
        buff.append('\n');
    }

    @Override
    public Expression[] getExpressions() {
        return new Expression[] {expression};
    }

    @Override
    public void replaceExpressions(Expression[] list) {
        assert (list.length == 1);
        expression = list[0];
        expression.setOwner(this);
    }

    private Expression expression;
}
