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

import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.bytecodebuilder.Return;

/**
 * The function
 */
public class Function extends Node implements DataTypeOwner, StatementListOwner {
    public Function(Fragment fragment, String name, ArgumentList arguments, DataType returnType, StatementList body) {
        this.fragment = fragment;
        this.name = name;
        this.arguments = arguments;
        if (arguments != null)
            arguments.setOwner(this);
        this.returnType = returnType;
        if (returnType != null)
            returnType.setOwner(this);
        this.body = body;
        body.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        if (arguments != null)
            arguments.dfs(visitor);
        if (returnType != null)
            returnType.dfs(visitor);
        body.dfs(visitor);
        accept(visitor);
    }

    void setOwner(FunctionOwner owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }

    public String getName() {
        return name;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append("FUNCTION ").append(name);
        if (arguments != null)
            arguments.toSourceCode(buff, i, i0);
        if (returnType != null) {
            buff.append(" AS ");
            returnType.toSourceCode(buff, i, i0);
        }
        buff.append('\n');
        String i1 = i + i0;
        body.toSourceCode(buff, i1, i0);
        buff.append(i).append("END FUNCTION\n");
    }

    public int getArgumentsCount() {
        return arguments != null ? arguments.getCount() : 0;
    }

    public DataType getReturnType() {
        return returnType;
    }

    public void compile(CompilationUnit cu) throws SyntaxError {
        body.compile(cu);
        cu.addInstruction(new Return());
    }

    private FunctionOwner owner;
    private Fragment fragment;
    private String name;
    private ArgumentList arguments;
    private DataType returnType;
    private StatementList body;
}
