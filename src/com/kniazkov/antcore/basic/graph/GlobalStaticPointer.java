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

import java.util.Map;

/**
 * The node represents pointer to a static global object (a constant, etc)
 */
public class GlobalStaticPointer extends Expression {
    public GlobalStaticPointer(Expression expression, Map<Module, Offset> addresses) {
        this.expression = expression;
        this.addresses = addresses;
    }

    @Override
    public DataType getType() {
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
    public void genLoad(CompilationUnit cu) {
        Module module = cu.getModule();
        assert(addresses.containsKey(module));
        cu.addInstruction(new PushGlobalPointer(cu.getStaticDataOffset(), addresses.get(module)));
    }

    private Expression expression;
    private Map<Module, Offset> addresses;
    private DataType type;
}
