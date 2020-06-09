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
import com.kniazkov.antcore.basic.bytecodebuilder.JumpIf;
import com.kniazkov.antcore.basic.common.DeferredOffset;
import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;

/**
 * If condition
 */
public class If extends Statement implements ExpressionOwner, StatementListOwner {
    public If(Fragment fragment, Expression condition, StatementList body) {
        super(fragment);
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
    public void compile(CompilationUnit unit) throws SyntaxError {
        condition.genLoad(unit);
        DeferredOffset jumpIfNot = new DeferredOffset();
        unit.addInstruction(new JumpIf(false, jumpIfNot));
        body.compile(unit);
        unit.resolveOffset(jumpIfNot);
    }

    @Override
    protected Node[] getChildren() {
        return new Node[] { condition, body };
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append("IF ");
        condition.toDeclarationSourceCode(buff, i);
        buff.append(" THEN\n");
        String i1 = i + i0;
        body.toSourceCode(buff, i1, i0);
        buff.append(i).append("END IF\n");
    }

    private Expression condition;
    private StatementList body;
}
