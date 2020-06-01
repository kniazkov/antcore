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
import com.kniazkov.antcore.basic.bytecodebuilder.PushShort;

/**
 * The node represents an short integer constant
 */
public class ShortNode extends Expression {
    public ShortNode(short value) {
        this.value = value;
    }

    @Override
    protected Node[] getChildren() {
        return null;
    }

    @Override
    public DataType getType() {
        return ShortType.getInstance();
    }

    @Override
    public Object calculate() {
        return value;
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
    public void genLoad(CompilationUnit unit) {
        unit.addInstruction(new PushShort(value));
    }

    private short value;
}
