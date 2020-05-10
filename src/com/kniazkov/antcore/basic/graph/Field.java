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

import com.kniazkov.antcore.basic.Fragment;
import com.kniazkov.antcore.basic.SyntaxError;
import com.kniazkov.antcore.lib.Reference;

/**
 * The node represents a field (of class, type, etc)
 */
public class Field extends LeftExpression implements DataTypeOwner {
    public Field(Fragment fragment, String name, DataType type) {
        this.fragment = fragment;
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
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        type.dfs(visitor);
        accept(visitor);
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
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append(name).append(" AS ");
        type.toSourceCode(buff, i, i0);
        buff.append("\n");
    }

    public int getOffset() {
        return offset;
    }

    void setOffset(int offset) {
        assert(offset == -1);
        this.offset = offset;
    }

    private DataSet owner;
    private Fragment fragment;
    private String name;
    private DataType type;
    private int offset;
}
