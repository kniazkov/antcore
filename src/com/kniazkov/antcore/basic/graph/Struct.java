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

import com.kniazkov.antcore.basic.bytecode.TypeSelector;
import com.kniazkov.antcore.basic.common.DataPrefix;
import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;

import java.util.List;

/**
 * The structure (TYPE..END TYPE), a data set wrapper
 */
public class Struct extends DataType implements DataSetOwner {
    public Struct(Fragment fragment, String name, List<Field> fields) {
        this.name = name;
        this.dataSet = new DataSet(fragment, DataPrefix.DEFAULT, fields);
        dataSet.setOwner(this);
        dataSet.setOffset(0);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        dataSet.dfs(visitor);
        accept(visitor);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSize() throws SyntaxError {
        return dataSet.getSize();
    }

    @Override
    public byte getSelector() {
        return TypeSelector.STRUCT;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public boolean isBuiltIn() {
        return false;
    }

    @Override
    public boolean isAbstract() {
        return false;
    }

    @Override
    void setOwner(DataTypeOwner owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    @Override
    public Fragment getFragment() {
        return dataSet.getFragment();
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append("TYPE ").append(name).append("\n");
        String i1 = i + i0;
        for (Field field : dataSet.getFields()) {
            field.toSourceCode(buff, i1, i0);
        }
        buff.append(i).append("END TYPE").append('\n');
    }

    @Override
    public boolean isBinaryAnalog(DataType otherType) throws SyntaxError {
        return otherType == this;
    }

    @Override
    public boolean isInheritedFrom(DataType otherType) {
        return false;
    }

    @Override
    public Expression staticCast(Expression expression, DataType otherType) throws SyntaxError {
        return null;
    }

    @Override
    public Expression dynamicCast(Expression expression, DataType otherType) {
        if (expression.getType().getPureType() == this)
            return expression;
        return null;
    }


    @Override
    public void calculateOffsets() throws SyntaxError {
        // do nothing
    }

    private DataTypeOwner owner;
    private String name;
    private DataSet dataSet;
}
