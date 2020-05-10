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
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        type.dfs(visitor);
        accept(visitor);
    }

    public String getName() {
        return name;
    }

    @Override
    void setOwner(ExpressionOwner owner) {
        this.owner = (ArgumentsList) owner;
    }

    void setOwner(ArgumentsList owner) {
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
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(name).append(" AS ");
        type.toSourceCode(buff, i, i0);
    }

    public int getOffset() {
        return offset;
    }

    void setOffset(int offset) {
        assert(offset == -1);
        this.offset = offset;
    }

    private ArgumentsList owner;
    private String name;
    private DataType type;
    private int offset;
}
