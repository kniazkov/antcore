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
import com.kniazkov.antcore.basic.bytecode.CompilationUnit;

/**
 * An expression, i.e. node that has a type
 */
public abstract class Expression extends Node {
    /**
     * @return type of the expression
     */
    public abstract DataType getType();

    /**
     * @return constant value of the expression
     */
    public abstract Object calculate();

    /**
     * Set owner of the node
     * @param owner owner
     */
    abstract void setOwner(ExpressionOwner owner);

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
        Object value = calculate();
        if (value != null)
            return value.toString();
        StringBuilder buff = new StringBuilder();
        toUsageSourceCode(buff);
        return buff.toString();
    }

    /**
     * Generate instructions for loading (calculating) value to the stack
     * @param cu the compilation unit
     */
    public abstract void load(CompilationUnit cu) throws SyntaxError;
}
