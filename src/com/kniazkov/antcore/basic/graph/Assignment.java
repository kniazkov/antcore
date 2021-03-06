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

import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.exceptions.ExpressionCannotBeAssigned;
import com.kniazkov.antcore.basic.exceptions.IncompatibleTypes;

/**
 * The assignment, i.e. '=' operation
 */
public class Assignment extends Statement implements ExpressionOwner {
    public Assignment(Fragment fragment, Expression left, Expression right) {
        super(fragment);
        this.left = left;
        left.setOwner(this);
        this.right = right;
        right.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        return new Node[] { right, left };
    }

    @Override
    public Expression[] getExpressions() {
        return new Expression[]{right};
    }

    @Override
    public void replaceExpressions(Expression[] list) {
        assert (list.length == 1);
        right = list[0];
        right.setOwner(this);
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i);
        left.toUsageSourceCode(buff);
        buff.append(" = ");
        right.toUsageSourceCode(buff);
        buff.append('\n');
    }

    /**
     * Check data type or inference it
     */
    void checkType() throws SyntaxError {
        Expression cast = DataTypeCast.cast(left.getType(), right);
        if (cast != right) {
            if (cast == null)
                throw new IncompatibleTypes(getFragment(), right.getType().getName(), left.getType().getName());
            right = cast;
            right.setOwner(this);
        }
    }

    @Override
    public void compile(CompilationUnit unit) throws SyntaxError {
        LeftExpression assignableExpression = left.toLeftExpression();
        if (assignableExpression == null)
            throw new ExpressionCannotBeAssigned(getFragment(), left.toString(), right.toString());
        right.genLoad(unit);
        assignableExpression.genStore(unit);
    }

    private Expression left;
    private Expression right;
}
