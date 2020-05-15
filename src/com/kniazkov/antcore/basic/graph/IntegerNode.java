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
import com.kniazkov.antcore.basic.bytecodebuilder.PushInteger;

/**
 * The node represents an integer constant
 */
public class IntegerNode extends Expression {
    public IntegerNode(int value) {
        this.value = value;
    }

    @Override
    public DataType getType() {
        return IntegerType.getInstance();
    }

    @Override
    public Object calculate() {
        return value;
    }

    @Override
    void setOwner(ExpressionOwner owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    @Override
    public void toDeclarationSourceCode(StringBuilder buff, String i) {
        buff.append(value);
    }

    @Override
    public void toUsageSourceCode(StringBuilder buff) {
        buff.append(value);
    }

    @Override
    public void load(CompilationUnit cu) {
        cu.addInstruction(new PushInteger(value));
    }

    private ExpressionOwner owner;
    private int value;
}
