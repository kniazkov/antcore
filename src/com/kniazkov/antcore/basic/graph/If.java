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
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.exceptions.ConditionMustBeBoolean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The 'IF' statement
 */
public class If extends Statement implements ExpressionOwner, StatementListOwner {
    public If(Fragment fragment, Expression condition, StatementList body, List<ElseIf> elseIfBlocks, Else elseBlock) {
        super(fragment);
        this.condition = condition;
        condition.setOwner(this);
        this.body = body;
        body.setOwner(this);
        if (elseIfBlocks != null) {
            this.elseIfBlocks = Collections.unmodifiableList(elseIfBlocks);
            for(ElseIf elseIfBlock : elseIfBlocks) {
                elseIfBlock.setOwner(this);
            }
        }
        this.elseBlock = elseBlock;
        if (elseBlock != null)
            elseBlock.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        List<Node> list = new ArrayList<>();
            list.add(condition);
        list.add(body);
        if (elseIfBlocks != null)
            list.addAll(elseIfBlocks);
        if (elseBlock != null)
            list.add(elseBlock);
        Node[] array = new Node[list.size()];
        list.toArray(array);
        return array;
    }

    @Override
    public void compile(CompilationUnit unit) throws SyntaxError {
        condition.genLoad(unit);
        DeferredOffset jumpIfNot = new DeferredOffset();
        DeferredOffset endAddress = null;
        unit.addInstruction(new JumpIf(false, jumpIfNot));
        body.compile(unit);
        if (elseIfBlocks != null) {
            endAddress = new DeferredOffset();
            for (ElseIf elseIfBlock : elseIfBlocks) {
                unit.addInstruction(new Jump(endAddress));
                unit.resolveAddress(jumpIfNot);
                elseIfBlock.getCondition().genLoad(unit);
                jumpIfNot = new DeferredOffset();
                unit.addInstruction(new JumpIf(false, jumpIfNot));
                elseIfBlock.getBody().compile(unit);
            }
        }
        if (elseBlock != null) {
            if (endAddress == null)
                endAddress = new DeferredOffset();
            unit.addInstruction(new Jump(endAddress));
            unit.resolveAddress(jumpIfNot);
            jumpIfNot = null;
            elseBlock.getBody().compile(unit);
        }

        if (jumpIfNot != null)
            unit.resolveAddress(jumpIfNot);
        if (endAddress != null)
            unit.resolveAddress(endAddress);
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append("IF ");
        condition.toDeclarationSourceCode(buff, i);
        buff.append(" THEN\n");
        String i1 = i + i0;
        body.toSourceCode(buff, i1, i0);
        if (elseIfBlocks != null) {
            for (ElseIf elseIfBlock : elseIfBlocks) {
                elseIfBlock.toSourceCode(buff, i, i0);
            }
        }
        if (elseBlock != null) {
            elseBlock.toSourceCode(buff, i, i0);
        }
        buff.append(i).append("END IF\n");
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
    private List<ElseIf> elseIfBlocks;
    private Else elseBlock;
}
