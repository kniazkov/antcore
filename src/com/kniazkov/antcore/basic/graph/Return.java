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

import com.kniazkov.antcore.basic.bytecode.DataSelector;
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.bytecodebuilder.Leave;
import com.kniazkov.antcore.basic.bytecodebuilder.ReturnInstruction;
import com.kniazkov.antcore.basic.bytecodebuilder.Store;
import com.kniazkov.antcore.basic.common.FixedOffset;
import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.common.ZeroOffset;

/**
 * The RETURN statement
 */
public class Return extends Statement implements ExpressionOwner {
    public Return(Fragment fragment, Expression value) {
        super(fragment);
        this.value = value;
        if (value != null)
            value.setOwner(this);
    }

    @Override
    public void compile(CompilationUnit unit) throws SyntaxError {
        Function function = getFunction();
        if (value != null) {
            value.genLoad(unit);
            unit.addInstruction(new Store(DataSelector.LOCAL,
                    value.getType().getSize(), ZeroOffset.getInstance(),
                    new FixedOffset(function.getReturnValueOffset())));
        }
        unit.addInstruction(new Leave(function.getLocalDataSize()));
        unit.addInstruction(new ReturnInstruction());
    }

    @Override
    protected Node[] getChildren() {
        return value != null ? new Node[]{ value } : null;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append("RETURN");
        if (value!= null) {
            buff.append(' ');
            value.toDeclarationSourceCode(buff, i);
        }
        buff.append('\n');
    }

    private Expression value;
}
