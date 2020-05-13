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

import com.kniazkov.antcore.basic.DataPrefix;
import com.kniazkov.antcore.basic.Fragment;
import com.kniazkov.antcore.basic.SyntaxError;

import java.util.List;

/**
 * The structure (TYPE..END TYPE), a data set wrapper
 */
public class Struct extends DataType implements DataSetOwner {
    public Struct(Fragment fragment, String name, List<Field> fields) {
        this.name = name;
        this.dataSet = new DataSet(fragment, DataPrefix.DEFAULT, fields);
        dataSet.setOwner(this);
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
    public boolean builtIn() {
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
    public boolean canBeCastTo(DataType otherType) throws SyntaxError {
        return this == otherType;
    }

    private DataTypeOwner owner;
    private String name;
    private DataSet dataSet;
}
