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
import com.kniazkov.antcore.basic.bytecodebuilder.Jump;
import com.kniazkov.antcore.basic.bytecodebuilder.JumpIf;
import com.kniazkov.antcore.basic.common.DeferredOffset;
import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.Offset;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.exceptions.ConditionMustBeBoolean;

/**
 * The 'DO..LOOP' cycle
 */
public class DoLoop extends Statement implements ExpressionOwner, StatementListOwner {
    public DoLoop(Fragment fragment, Expression condition, StatementList body,
                  boolean postCondition, boolean negativeCondition) {
        super(fragment);
        this.condition = condition;
        condition.setOwner(this);
        this.body = body;
        body.setOwner(this);
        this.postCondition = postCondition;
        this.negativeCondition = negativeCondition;
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void compile(CompilationUnit unit) throws SyntaxError {
        Offset begin = unit.getCurrentAddress();
        DeferredOffset end = new DeferredOffset();
        if (!postCondition) {
            condition.genLoad(unit);
            unit.addInstruction(new JumpIf(negativeCondition, end));
        }
        body.compile(unit);
        if (!postCondition) {
            unit.addInstruction(new Jump(begin));
        }
        else {
            condition.genLoad(unit);
            unit.addInstruction(new JumpIf(!negativeCondition, begin));
        }
        unit.resolveAddress(end);
    }

    @Override
    protected Node[] getChildren() {
        return new Node[] { condition, body };
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append("DO");
        if (!postCondition) {
            buff.append(' ').append(negativeCondition ? "UNTIL" : "WHILE").append(' ');
            condition.toUsageSourceCode(buff);
        }
        buff.append('\n');
        body.toSourceCode(buff, i + i0, i0);
        buff.append(i).append("LOOP");
        if (postCondition) {
            buff.append(' ').append(negativeCondition ? "UNTIL" : "WHILE").append(' ');
            condition.toUsageSourceCode(buff);
        }
        buff.append('\n');
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

    private Expression condition;
    private StatementList body;
    private boolean postCondition;
    private boolean negativeCondition;
}
