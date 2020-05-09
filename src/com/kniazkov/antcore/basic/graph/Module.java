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

import com.kniazkov.antcore.basic.Fragment;
import com.kniazkov.antcore.basic.SyntaxError;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The node that represents a module
 */
public class Module extends Node implements DataSetOwner, FunctionOwner {
    public Module(Fragment fragment, String name, String executor,
                  DataSet localData, DataSet inputData, DataSet outputData,
                  List<Function> functions) {
        this.fragment = fragment;
        this.name = name;
        this.executor = executor;
        this.localData = localData;
        if (localData != null)
            localData.setOwner(this);
        this.inputData = inputData;
        if (inputData != null)
            inputData.setOwner(this);
        this.outputData = outputData;
        if (outputData != null)
            outputData.setOwner(this);
        this.functionList = Collections.unmodifiableList(functions);
        this.functionMap = new TreeMap<>();
        for (Function function : functions) {
            function.setOwner(this);
            functionMap.put(function.getName(), function);
        }
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        if (localData != null)
            localData.dfs(visitor);
        if (inputData != null)
            inputData.dfs(visitor);
        if (outputData != null)
            outputData.dfs(visitor);
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

    public String getName() {
        return name;
    }

    public String getExecutor() {
        return executor;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        if (executor != null)
            buff.append(i).append("MODULE ").append(name).append(" ").append(executor).append("\n");
        else
            buff.append(i).append("MODULE ").append(name).append("\n");
        String i1 = i + i0;
        boolean flag = false;
        if (localData != null) {
            localData.toSourceCode(buff, i1, i0);
            flag = true;
        }
        if (inputData != null) {
            if (flag)
                buff.append('\n');
            inputData.toSourceCode(buff, i1, i0);
            flag = true;
        }
        if (outputData != null) {
            if (flag)
                buff.append('\n');
            outputData.toSourceCode(buff, i1, i0);
            flag = true;
        }
        if (functionMap.containsKey("MAIN")) {
            Function mainFunction = functionMap.get("MAIN");
            if (flag)
                buff.append('\n');
            mainFunction.toSourceCode(buff, i1, i0);
            flag = true;
        }
        for (Function function : functionList) {
            if (!function.getName().equals("MAIN")) {
                if (flag)
                    buff.append('\n');
                function.toSourceCode(buff, i1, i0);
                flag = true;
            }
        }
        buff.append(i).append("END MODULE").append("\n");
    }

    private Program owner;
    private Fragment fragment;
    private String name;
    private String executor;
    private DataSet localData;
    private DataSet inputData;
    private DataSet outputData;
    private List<Function> functionList;
    private Map<String, Function> functionMap;
}
