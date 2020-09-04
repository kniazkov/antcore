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
import com.kniazkov.antcore.basic.exceptions.ConditionMustBeBoolean;

/**
 * The 'ELSE IF' block
 */
public class ElseIf extends Node implements ExpressionOwner, StatementListOwner {
    public ElseIf(Fragment fragment, Expression condition, StatementList body) {
        this.fragment = fragment;
        this.condition = condition;
        condition.setOwner(this);
        this.body = body;
        body.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        return new Node[] { condition, body };
    }

    @Override
    public Node getOwner() {
        return owner;
    }

    void setOwner(If owner) {
        this.owner = owner;
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public Function getFunction() {
        return owner.getFunction();
    }

    public Expression getCondition() {
        return condition;
    }

    public StatementList getBody() {
        return body;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append("ELSE IF ");
        condition.toDeclarationSourceCode(buff, i);
        buff.append(" THEN\n");
        String i1 = i + i0;
        body.toSourceCode(buff, i1, i0);
    }

    @Override
    public Expression[] getExpressions() {
        return new Expression[] {condition};
    }

    @Override
    public void replaceExpressions(Expression[] list) {
        assert (list.length == 1);
        condition = list[0];
        condition.setOwner(this);
    }

    void checkType() throws SyntaxError {
        Expression cast = DataTypeCast.cast(BooleanType.getInstance(), condition);
        if (cast != condition) {
            if (cast == null)
                throw new ConditionMustBeBoolean(getFragment(), condition.getType().getName());
            condition = cast;
            cast.setOwner(this);
        }
    }

    private If owner;
    private Fragment fragment;
    private Expression condition;
    private StatementList body;
}
