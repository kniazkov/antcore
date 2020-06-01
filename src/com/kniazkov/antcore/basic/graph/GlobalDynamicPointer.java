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
import com.kniazkov.antcore.basic.bytecodebuilder.PushGlobalPointer;
import com.kniazkov.antcore.basic.common.Offset;
import com.kniazkov.antcore.basic.common.SyntaxError;

/**
 * The node represents pointer to a dynamic global object (a field, etc)
 */
public class GlobalDynamicPointer extends Expression {
    public GlobalDynamicPointer(Expression expression, Offset address) {
        this.expression = expression;
        this.address = address;
    }

    @Override
    protected Node[] getChildren() {
        if (type != null)
            return new Node[] { expression, type };
        return new Node[] { expression };
    }

    @Override
    public DataType getType() throws SyntaxError {
        if (type == null)
            type = new Pointer(expression.getType());
        return type;
    }

    @Override
    public Object calculate() {
        return null;
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
    public void genLoad(CompilationUnit unit) {
        unit.addInstruction(new PushGlobalPointer(unit.getDynamicDataOffset(), address));
    }

    private Expression expression;
    private Offset address;
    private DataType type;
}
