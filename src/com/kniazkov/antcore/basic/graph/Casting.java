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

import com.kniazkov.antcore.basic.bytecodebuilder.Cast;
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.lib.Variant;

/**
 * Expression casted to other type
 */
public class Casting extends Expression implements ExpressionOwner, DataTypeOwner {
    public Casting(boolean implicit, Expression expression, DataType newType) {
        this.implicit = implicit;
        this.expression = expression;
        this.newType = newType;
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        return new Node[] { expression, newType };
    }

    @Override
    public DataType getType() {
        return newType;
    }

    @Override
    public Variant calculate() {
        return expression.calculate();
    }

    @Override
    public void toDeclarationSourceCode(StringBuilder buff, String i) {
        toUsageSourceCode(buff);
    }

    @Override
    public void toUsageSourceCode(StringBuilder buff) {
        if (implicit)
            expression.toUsageSourceCode(buff);
        else {
            buff.append("CAST(");
            expression.toUsageSourceCode(buff);
            buff.append(", ").append(newType.toString()).append(')');
        }
    }

    @Override
    public void genLoad(CompilationUnit unit) throws SyntaxError {
        expression.genLoad(unit);
        DataType currentType = expression.getType();
        Cast cast = new Cast(currentType.getSelector(), currentType.getSize(),
                newType.getSelector(), newType.getSize());
        unit.addInstruction(cast);
    }

    private boolean implicit;
    private Expression expression;
    private DataType newType;
}
