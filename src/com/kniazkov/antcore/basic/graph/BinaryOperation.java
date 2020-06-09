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

import com.kniazkov.antcore.basic.common.SyntaxError;

import java.util.Arrays;
import java.util.List;

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
    protected Node[] getChildren() {
        return new Node[] { left, right };
    }

    @Override
    public DataType getType() throws SyntaxError {
        if (type == null)
            defineType();
        return type;
    }

    protected void setType(DataType type) {
        this.type = type;
    }

    @Override
    public void toDeclarationSourceCode(StringBuilder buff, String i) {
        toUsageSourceCode(buff);
    }

    @Override
    public void toUsageSourceCode(StringBuilder buff) {
        left.toUsageSourceCode(buff);
        buff.append(" ").append(getOperator()).append(" ");
        right.toUsageSourceCode(buff);
    }

    protected DataType getLeftPureNonConstantType () throws SyntaxError {
        DataType type = left.getType().getPureType();
        if (type instanceof ConstantModifier)
            return ((ConstantModifier) type).getNonConstantType();
        return type;
    }

    protected DataType getRightPureNonConstantType () throws SyntaxError {
        DataType type = right.getType().getPureType();
        if (type instanceof ConstantModifier)
            return ((ConstantModifier) type).getNonConstantType();
        return type;
    }

    protected abstract String getOperator();
    abstract void defineType() throws SyntaxError;

    private DataType type;
    protected Expression left;
    protected Expression right;
}
