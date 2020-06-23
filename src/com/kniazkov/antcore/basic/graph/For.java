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
import com.kniazkov.antcore.basic.bytecode.TypeSelector;
import com.kniazkov.antcore.basic.bytecodebuilder.*;
import com.kniazkov.antcore.basic.common.*;
import com.kniazkov.antcore.basic.exceptions.CounterMustBeNumeric;
import com.kniazkov.antcore.basic.exceptions.ExpressionCannotBeAssigned;
import com.kniazkov.antcore.basic.exceptions.IncompatibleTypes;

/**
 * The 'FOR' statement
 */
public class For extends Statement implements  ExpressionOwner, StatementListOwner {
    public For(Fragment fragment, Expression variable, Expression start, Expression end, Expression step,
               StatementList body) {
        super(fragment);
        this.variable = variable;
        variable.setOwner(this);
        this.start = start;
        start.setOwner(this);
        this.end = end;
        end.setOwner(this);
        this.step = step;
        if (step != null)
            step.setOwner(this);
        this.body = body;
        body.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    public Expression getCounter() {
        return variable;
    }

    public Expression getStart() {
        return start;
    }

    @Override
    public void compile(CompilationUnit unit) throws SyntaxError {
        LeftExpression assignableExpression = variable.toLeftExpression();
        if (assignableExpression == null)
            throw new ExpressionCannotBeAssigned(getFragment(), variable.toString(), start.toString());
        DataType counterType = variable.getType();
        int counterSize = counterType.getSize();
        byte counterSelector = counterType.getSelector();
        Function function = getFunction();

        // evaluate the end
        Variable tmpVarEnd = function.createTemporaryVariable(counterType);
        end.genLoad(unit);
        tmpVarEnd.genStore(unit);

        // evaluate the step and the sign of the step
        Variable tmpVarStep = function.createTemporaryVariable(counterType);
        Variable tmpVarStepSign = function.createTemporaryVariable(ByteType.getInstance());
        step.genLoad(unit);
        unit.addInstruction(new Dup(counterType.getSize()));
        tmpVarStep.genStore(unit);
        unit.addInstruction(new Sign(counterSelector));
        tmpVarStepSign.genStore(unit);

        // evaluate the start
        start.genLoad(unit);
        assignableExpression.genStore(unit);

        // begin of the body - compare the variable with the end value and calculate the sign of the difference
        Offset beginAddress = unit.getCurrentAddress();
        tmpVarEnd.genLoad(unit);
        assignableExpression.genLoad(unit);
        unit.addInstruction(new Sub(counterSelector, counterSize, counterSize, counterSize));
        unit.addInstruction(new Sign(counterSelector));
        tmpVarStepSign.genLoad(unit);
        unit.addInstruction(new Compare(TypeSelector.BYTE, ComparatorSelector.EQUAL, 1, 1));
        DeferredOffset endAddress = new DeferredOffset();
        unit.addInstruction(new JumpIf(true, endAddress));

        // the body
        body.compile(unit);

        // end of the body - increment the counter
        assignableExpression.genLoad(unit);
        tmpVarStep.genLoad(unit);
        unit.addInstruction(new Add(counterSelector, counterSize, counterSize, counterSize));
        assignableExpression.genStore(unit);
        unit.addInstruction(new Jump(beginAddress));
        unit.resolveAddress(endAddress);
    }

    @Override
    protected Node[] getChildren() {
        if (step != null)
            return new Node[] { variable, start, end, step, body };
        return new Node[] { variable, start, end, body };
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append("FOR ");
        variable.toUsageSourceCode(buff);
        buff.append(" = ");
        start.toUsageSourceCode(buff);
        buff.append(" TO ");
        end.toUsageSourceCode(buff);
        if (step != null) {
            buff.append(" STEP ");
            step.toUsageSourceCode(buff);
        }
        buff.append('\n');
        body.toSourceCode(buff, i + i0, i0);
        buff.append(i).append("NEXT ");
        variable.toUsageSourceCode(buff);
        buff.append('\n');
    }

    void checkTypes() throws SyntaxError {
        DataType counterType = variable.getType();
        if (!counterType.isNumeric())
            throw new CounterMustBeNumeric(getFragment());
        Expression cast = DataTypeCast.cast(counterType, start);
        if (cast != start) {
            if (cast == null)
                throw new IncompatibleTypes(getFragment(), start.getType().getName(), counterType.getName());
            start = cast;
            start.setOwner(this);
        }
        cast = DataTypeCast.cast(counterType, end);
        if (cast != end) {
            if (cast == null)
                throw new IncompatibleTypes(getFragment(), end.getType().getName(), counterType.getName());
            end = cast;
            end.setOwner(this);
        }
        if (step == null) {
            step = DataTypeCast.cast(counterType, new IntegerNode(1));
            assert(step != null);
            step.setOwner(this);
        }
        else {
            cast = DataTypeCast.cast(counterType, step);
            if (cast != step) {
                if (cast == null)
                    throw new IncompatibleTypes(getFragment(), step.getType().getName(), counterType.getName());
                step = cast;
                step.setOwner(this);
            }
        }
    }

    private Expression variable;
    private Expression start;
    private Expression end;
    private Expression step;
    private StatementList body;
}
