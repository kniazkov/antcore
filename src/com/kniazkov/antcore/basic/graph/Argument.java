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
import com.kniazkov.antcore.basic.bytecodebuilder.Load;
import com.kniazkov.antcore.basic.bytecodebuilder.RawInstruction;
import com.kniazkov.antcore.basic.bytecodebuilder.Store;
import com.kniazkov.antcore.basic.common.FixedOffset;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.common.ZeroOffset;

import java.util.Collections;
import java.util.List;

/**
 * The node represents an function (method) argument
 */
public class Argument extends LeftExpression implements DataTypeOwner {
    public Argument(String name, DataType type) {
        this.name = name;
        this.type = type;
        type.setOwner(this);
        this.offset = -1;
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        return new Node[]{type};
    }

    public String getName() {
        return name;
    }

    @Override
    void setOwner(ExpressionOwner owner) {
        this.owner = (ArgumentList) owner;
    }

    void setOwner(ArgumentList owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return owner;
    }
    
    @Override
    public DataType getType() {
        return type;
    }

    @Override
    public void toDeclarationSourceCode(StringBuilder buff, String i) {
        buff.append(name).append(" AS ").append(type.getName());
    }

    @Override
    public void toUsageSourceCode(StringBuilder buff) {
        buff.append(name);
    }

    public int getOffset() {
        return offset;
    }

    void setOffset(int offset) {
        assert(this.offset == -1);
        this.offset = offset;
    }

    @Override
    public void genLoad(CompilationUnit unit) throws SyntaxError {
        RawInstruction load = new Load(DataSelector.LOCAL,
                type.getSize(), ZeroOffset.getInstance(),
                new FixedOffset(offset + owner.getFunction().getFirstArgumentOffset()));
        unit.addInstruction(load);
    }

    @Override
    public void genStore(CompilationUnit unit) throws SyntaxError {
        RawInstruction store = new Store(DataSelector.LOCAL,
                type.getSize(), ZeroOffset.getInstance(),
                new FixedOffset(offset + owner.getFunction().getFirstArgumentOffset()));
        unit.addInstruction(store);
    }

    private ArgumentList owner;
    private String name;
    private DataType type;
    private int offset;
}
