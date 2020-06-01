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
import com.kniazkov.antcore.basic.exceptions.CannotResolveSymbol;

/**
 * The reference to variable (local variable, field of data set, argument)
 */
public class VariableReference extends Expression implements ExpressionOwner {
    public VariableReference(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        return expression != null ? new Node[] { expression } : null;
    }

    public String getName() {
        return name;
    }

    @Override
    public DataType getType() throws SyntaxError {
        return expression.getType();
    }

    @Override
    public Object calculate() {
        return expression != null ? expression.calculate() : null;
    }

    @Override
    public void toDeclarationSourceCode(StringBuilder buff, String i) {
        toUsageSourceCode(buff);
    }

    @Override
    public void toUsageSourceCode(StringBuilder buff) {
        if (expression != null)
            expression.toUsageSourceCode(buff);
        else
            buff.append(name);
    }

    /**
     * Bind variable by name
     */
    void bindName() throws SyntaxError {
        if (expression != null)
            return;
        expression = findVariableByName(name);
        if (expression == null) {
            Node owner = getOwner();
            if (owner instanceof Assignment) {
                Assignment assignment = (Assignment) owner;
                if (assignment.getLeft() == this) {
                    StatementList assignmentOwner = (StatementList) assignment.getOwner();
                    Function function = assignmentOwner.getFunction();
                    Variable newVariable = function.createVariable(name, assignment.getRight().getType().getPureType());
                    assignmentOwner.addVariable(newVariable);
                    expression = newVariable;
                    return;
                }
            }
            throw new CannotResolveSymbol(getFragment(), name);
        }
    }

    @Override
    public LeftExpression toLeftExpression() {
        return expression.toLeftExpression();
    }

    @Override
    public void genLoad(CompilationUnit unit) throws SyntaxError {
        expression.genLoad(unit);
    }

    @Override
    public Expression getPointer() throws SyntaxError {
        return expression.getPointer();
    }

    private String name;
    private Expression expression;
}
