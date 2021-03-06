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
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.lib.Variant;

/**
 * An expression, i.e. node that has a type
 */
public abstract class Expression extends Node {
    /**
     * @return type of the expression
     */
    public abstract DataType getType() throws SyntaxError;

    /**
     * @return constant value of the expression
     */
    public abstract Variant calculate();

    /**
     * @return true if this is an atomic expression, that is, cannot be divided into parts
     */
    public boolean isAtomic() {
        return false;
    }

    /**
     * Set owner of the node
     * @param owner owner
     */
    void setOwner(ExpressionOwner owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        toDeclarationSourceCode(buff, i);
    }

    /**
     * Generate source code for expression declaration
     * @param buff destination buffer
     * @param i indentation
     */
    public abstract void toDeclarationSourceCode(StringBuilder buff, String i);

    /**
     * Generate source code for expression usage
     * @param buff destination buffer
     */
    public abstract void toUsageSourceCode(StringBuilder buff);

    /**
     * Converts the expression to left expression, if possible
     * @return a left expression
     */
    public LeftExpression toLeftExpression() {
        return null;
    }

    @Override
    public String toString() {
        Variant value = calculate();
        if (!value.isNull())
            return value.toString();
        StringBuilder buff = new StringBuilder();
        toUsageSourceCode(buff);
        return buff.toString();
    }

    /**
     * Generate instructions for loading (calculating) value to the stack
     * @param unit the compilation unit
     */
    public abstract void genLoad(CompilationUnit unit) throws SyntaxError;

    /**
     * Generate pointer to the expression (if possible)
     * @return A pointer
     */
    public Expression getPointer() throws SyntaxError {
        Statement statement = getStatement();
        if (statement != null) {
            Function function = statement.getFunction();
            Variable tmpVar = function.createTemporaryVariable(getType());
            return new TemporaryPointer(this, tmpVar);
        }
        return null;
    }

    /**
     * Get a statement containing this expression
     * @return function
     */
    public Statement getStatement() {
        Node node = getOwner();
        while(node != null) {
            if (node instanceof Statement)
                return (Statement)node;
            node = node.getOwner();
        }
        return null;
    }

    private ExpressionOwner owner;
}
