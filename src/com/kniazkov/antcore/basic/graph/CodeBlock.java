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
    public CodeBlock(Fragment fragment, List<String> executors, List<NativeFunction> nativeFunctions,
                     List<Function> functions) {
        this.fragment = fragment;
        if (executors != null)
            this.executors = Collections.unmodifiableList(executors);
        List<BaseFunction> functionList = new ArrayList<>();
        allFunctionMap = new TreeMap<>();
        this.nativeFunctionList = Collections.unmodifiableList(nativeFunctions);
        for (NativeFunction function : nativeFunctions) {
            function.setOwner(this);
            functionList.add(function);
            allFunctionMap.put(function.getName(), function);
        }
        this.functionList = Collections.unmodifiableList(functions);
        for (Function function : functions) {
            function.setOwner(this);
            functionList.add(function);
            allFunctionMap.put(function.getName(), function);
        }
        allFunctionList = Collections.unmodifiableList(functionList);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        List<Node> list = new ArrayList<>();
        list.addAll(nativeFunctionList);
        list.addAll(functionList);
        Node[] array = new Node[list.size()];
        list.toArray(array);
        return array;
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
        boolean flag2 = false;
        for (NativeFunction function : nativeFunctionList) {
            function.toSourceCode(buff, i1, i0);
            flag2 = true;
        }
        for (Function function : functionList) {
            if (flag2)
                buff.append('\n');
            function.toSourceCode(buff, i1, i0);
            flag2 = true;
        }
        buff.append(i).append("END CODE\n");
    }

    public List<String> getExecutors() {
        return executors;
    }

    public List<BaseFunction> getFunctionList() {
        return allFunctionList;
    }

    @Override
    protected BaseFunction findFunctionByName(String name) {
        return allFunctionMap.get(name);
    }

    private Program owner;
    private Fragment fragment;
    private List<String> executors;
    private List<NativeFunction> nativeFunctionList;
    private List<Function> functionList;
    private List<BaseFunction> allFunctionList;
    private Map<String, BaseFunction> allFunctionMap;
}
