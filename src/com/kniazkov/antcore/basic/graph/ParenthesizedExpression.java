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

/**
 * Node represents expression in brackets
 */
public class ParenthesizedExpression extends Expression implements ExpressionOwner {
    public ParenthesizedExpression(Expression expression) {
        this.expression = expression;
        expression.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        return new Node[] { expression };
    }

    @Override
    public DataType getType() {
        return expression.getType();
    }

    @Override
    public Object calculate() {
        return expression.calculate();
    }

    @Override
    public void toDeclarationSourceCode(StringBuilder buff, String i) {
        toUsageSourceCode(buff);
    }

    @Override
    public void toUsageSourceCode(StringBuilder buff) {
        buff.append('(');
        expression.toUsageSourceCode(buff);
        buff.append(')');
    }

    @Override
    public void genLoad(CompilationUnit cu) throws SyntaxError {
        expression.genLoad(cu);
    }

    private Expression expression;
}
