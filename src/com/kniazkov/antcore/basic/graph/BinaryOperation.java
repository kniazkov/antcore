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

import com.kniazkov.antcore.basic.SyntaxError;

/**
 * A binary operation
 */
public abstract class BinaryOperation extends Expression implements ExpressionOwner {
    public BinaryOperation(Expression left, Expression right) {
        this.left = left;
        left.setOwner(this);
        this.right = right;
        right.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        left.dfs(visitor);
        right.dfs(visitor);
        accept(visitor);
    }

    @Override
    public DataType getType() {
        return type;
    }

    protected void setType(DataType type) {
        this.type = type;
    }

    @Override
    void setOwner(ExpressionOwner owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    protected abstract String getOperator();
    abstract void defineType() throws SyntaxError;

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        left.toSourceCode(buff, i, i0);
        buff.append(" ").append(getOperator()).append(" ");
        right.toSourceCode(buff, i, i0);
    }

    private DataType type;
    private ExpressionOwner owner;
    protected Expression left;
    protected Expression right;
}
