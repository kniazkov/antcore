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

import com.kniazkov.antcore.basic.bytecodebuilder.End;
import com.kniazkov.antcore.basic.bytecodebuilder.StaticDataBuilder;
import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.bytecode.CompiledModule;
import com.kniazkov.antcore.basic.exceptions.DuplicateField;
import com.kniazkov.antcore.basic.exceptions.DuplicateFunction;
import com.kniazkov.antcore.basic.exceptions.FunctionMainNotFound;
import com.kniazkov.antcore.basic.exceptions.IncorrectFunctionMain;

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
        this.staticData = new StaticDataBuilder(this);
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
        for (Function function : functionList) {
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

    public String getName() {
        return name;
    }

    public String getNotNullExecutor() {
        return executor != null ? executor : "SERVER";
    }

    StaticDataBuilder getStaticData() {
        return staticData;
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

    @Override
    protected Expression findVariableByName(String name) {
        Expression ex = null;
        if (localData != null) {
            ex = localData.getFieldByName(name);
            if (ex != null)
                return ex;
        }
        if (inputData != null) {
            ex = inputData.getFieldByName(name);
            if (ex != null)
                return ex;
        }
        if (outputData != null) {
            ex = outputData.getFieldByName(name);
            if (ex != null)
                return ex;
        }
        return owner.findVariableByName(name);
    }

    @Override
    protected BaseFunction findFunctionByName(String name) {
        return allFunctionMap.get(name);
    }

    @Override
    public void calculateOffsets() throws SyntaxError {
        int offset = 0;
        if (localData != null) {
            localData.setOffset(0);
            offset = localData.getSize();
        }
        if (inputData != null) {
            inputData.setOffset(offset);
            offset += inputData.getSize();
        }
        if (outputData != null) {
            outputData.setOffset(offset);
        }
    }

    /**
     * Merging all appropriate code blocks to this module
     */
    void mergeCode() throws SyntaxError {
        List<CodeBlock> blocks = owner.getCodeBlocksByExecutor(getNotNullExecutor());
        allFunctionMap = new TreeMap<>(functionMap);
        for (CodeBlock block : blocks) {
            List<BaseFunction> functions = block.getFunctionList();
            for (BaseFunction function : functions) {
                String name = function.getName();
                if (allFunctionMap.containsKey(name)) {
                    BaseFunction duplicate = allFunctionMap.get(name);
                    throw new DuplicateFunction(duplicate.getFragment(), name);
                }
                allFunctionMap.put(name, function);
            }
        }
    }

    /**
     * Compiling the module
     * @return a bytecode
     */
    public CompiledModule compile() throws SyntaxError {
        Function mainFunction = functionMap.get("MAIN");
        if (mainFunction == null)
            throw new FunctionMainNotFound(fragment, name);
        if (mainFunction.getArgumentsCount() != 0 || mainFunction.getReturnType() != null)
            throw new IncorrectFunctionMain(fragment);
        CompilationUnit unit = new CompilationUnit(this, staticData);
        mainFunction.compile(unit);
        unit.addInstruction(new End());
        return new CompiledModule(getNotNullExecutor(), name, unit.getBytecode());
    }

    private Program owner;
    private Fragment fragment;
    private String name;
    private String executor;
    private StaticDataBuilder staticData;
    private DataSet localData;
    private DataSet inputData;
    private DataSet outputData;
    private List<Function> functionList;
    private Map<String, Function> functionMap;
    private Map<String, BaseFunction> allFunctionMap;
}
