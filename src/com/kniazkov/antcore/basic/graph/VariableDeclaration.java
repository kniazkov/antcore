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

import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.exceptions.ExpressionCannotBeAssigned;

/**
 * The node represents a variable declaration
 */
public class VariableDeclaration extends Statement {

    public VariableDeclaration(Fragment fragment, Variable variable) {
        super(fragment);
        this.variable = variable;
        variable.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        variable.dfs(visitor);
        accept(visitor);
    }

    void setOwner(StatementList owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return owner;
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public void compile(CompilationUnit cu) throws SyntaxError {
        Expression initValue = variable.getInitValue();
        if (initValue != null) {
            initValue.genLoad(cu);
            variable.genStore(cu);
        }
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append("VAR ");
        variable.toDeclarationSourceCode(buff, i);
        buff.append('\n');
    }

    private StatementList owner;
    private Variable variable;
}
