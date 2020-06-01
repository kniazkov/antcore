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

import com.kniazkov.antcore.basic.common.DeferredOffset;
import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.Offset;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.bytecode.*;
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.bytecodebuilder.RawInstruction;
import com.kniazkov.antcore.basic.bytecodebuilder.Load;
import com.kniazkov.antcore.basic.bytecodebuilder.Store;

/**
 * The node represents a field (of class, type, etc)
 */
public class Field extends LeftExpression implements DataTypeOwner {
    public Field(Fragment fragment, String name, DataType type) {
        this.fragment = fragment;
        this.name = name;
        this.type = type;
        type.setOwner(this);
        offset = new DeferredOffset();
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        return new Node[]{ type };
    }

    public String getName() {
        return name;
    }

    void setOwner(ExpressionOwner owner) {
        this.owner = (DataSet) owner;
    }

    void setOwner(DataSet owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return owner;
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public DataType getType() {
        return type;
    }

    @Override
    public void toDeclarationSourceCode(StringBuilder buff, String i) {
        buff.append(i).append(name).append(" AS ").append(type.getName()).append('\n');
    }

    @Override
    public void toUsageSourceCode(StringBuilder buff) {
        buff.append(name);
    }

    private static class AbsoluteOffset implements Offset {
        @Override
        public int get() {
            return dataSetOffset.get() + fieldOffset.get();
        }

        Offset dataSetOffset;
        Offset fieldOffset;
    }

    public Offset getAbsoluteOffset() {
        if (absoluteOffset == null) {
            absoluteOffset = new AbsoluteOffset();
            absoluteOffset.dataSetOffset = owner.getOffset();
            absoluteOffset.fieldOffset = this.offset;
        }
        return absoluteOffset;
    }

    void setOffset(int value) {
        offset.resolve(value);
    }

    @Override
    public void genLoad(CompilationUnit unit) throws SyntaxError {
        RawInstruction load = new Load(DataSelector.GLOBAL,
                type.getSize(), unit.getDynamicDataOffset(), getAbsoluteOffset());
        unit.addInstruction(load);
    }

    @Override
    public void genStore(CompilationUnit unit) throws SyntaxError {
        RawInstruction store = new Store(DataSelector.GLOBAL,
                type.getSize(), unit.getDynamicDataOffset(), getAbsoluteOffset());
        unit.addInstruction(store);
    }

    @Override
    public Expression getPointer() {
        return new GlobalDynamicPointer(this, getAbsoluteOffset());
    }

    private DataSet owner;
    private Fragment fragment;
    private String name;
    private DataType type;
    private DeferredOffset offset;
    private AbsoluteOffset absoluteOffset;
}
