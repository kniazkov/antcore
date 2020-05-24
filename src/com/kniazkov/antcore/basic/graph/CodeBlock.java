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

import java.util.*;

/**
 * The node that represents a block of code
 */
public class CodeBlock extends Node implements FunctionOwner {
    public CodeBlock(Fragment fragment, List<String> executors, List<NativeFunction> nativeFunctions) {
        this.fragment = fragment;
        if (executors != null)
            this.executors = Collections.unmodifiableList(executors);
        this.nativeFunctionList = Collections.unmodifiableList(nativeFunctions);
        List<BaseFunction> functionList = new ArrayList<>();
        for (NativeFunction function : nativeFunctions) {
            function.setOwner(this);
            functionList.add(function);
        }
        allFunctionList = Collections.unmodifiableList(functionList);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        for (NativeFunction function : nativeFunctionList) {
            function.dfs(visitor);
        }
        accept(visitor);
    }

    void setOwner(Program owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return owner;
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        if (executors != null) {
            buff.append(i).append("CODE ");
            boolean flag = false;
            for (String executor : executors) {
                if (flag)
                    buff.append(", ");
                buff.append(executor);
                flag = true;
            }
            buff.append("\n");
        }
        else {
            buff.append(i).append("CODE\n");
        }
        String i1 = i + i0;
        for (NativeFunction function : nativeFunctionList) {
            function.toSourceCode(buff, i1, i0);
        }
        buff.append(i).append("END CODE\n");
    }

    public List<String> getExecutors() {
        return executors;
    }

    public List<BaseFunction> getFunctionList() {
        return allFunctionList;
    }

    private Program owner;
    private Fragment fragment;
    private List<String> executors;
    private List<NativeFunction> nativeFunctionList;
    private List<BaseFunction> allFunctionList;
}
