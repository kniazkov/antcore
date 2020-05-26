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
import com.kniazkov.antcore.basic.bytecodebuilder.PushInteger;
import com.kniazkov.antcore.lib.Reference;

/**
 * The node represents a global pointer, i.e. pointer to global variable or field of global object
 */
public class GlobalPointer extends Expression {
    public GlobalPointer(Expression expression, int address, boolean isDynamicData) {
        this.expression = expression;
        this.address = address;
        this.isDynamicData = isDynamicData;
    }

    @Override
    public DataType getType() {
        if (type == null)
            type = new Pointer(expression.getType());
        return type;
    }

    @Override
    public Object calculate() {
        return address;
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
        Reference<Integer> offset = isDynamicData ? cu.getDynamicDataOffset() : cu.getStaticDataOffset();
        cu.addInstruction(new PushGlobalPointer(offset, address));
    }

    private Expression expression;
    private int address;
    private DataType type;
    private boolean isDynamicData;
}
