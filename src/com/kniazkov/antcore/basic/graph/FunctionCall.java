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
import com.kniazkov.antcore.basic.common.SyntaxError;

import java.util.List;

/**
 * The function (method) call
 */
public class FunctionCall extends Expression implements ExpressionOwner {
    public FunctionCall(String functionName, List<Expression> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
        for (Expression argument : arguments) {
            argument.setOwner(this);
        }
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        for (Expression argument : arguments) {
            argument.dfs(visitor);
        }
        accept(visitor);
    }

    @Override
    public DataType getType() {
        return function.getReturnType();
    }

    @Override
    public Object calculate() {
        return null;
    }

    @Override
    public void toDeclarationSourceCode(StringBuilder buff, String i) {
        toUsageSourceCode(buff);
    }

    @Override
    public void toUsageSourceCode(StringBuilder buff) {
        buff.append(functionName).append('(');
        boolean flag = false;
        for (Expression argument : arguments) {
            if (flag)
                buff.append(", ");
            argument.toUsageSourceCode(buff);
            flag = true;
        }
        buff.append(')');
    }

    @Override
    public void genLoad(CompilationUnit cu) throws SyntaxError {
        // TODO
    }

    private String functionName;
    private BaseFunction function;
    private List<Expression> arguments;
}
