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

import com.kniazkov.antcore.basic.bytecode.Binding;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.bytecode.CompiledModule;
import com.kniazkov.antcore.basic.bytecode.CompiledProgram;

import java.util.*;

/**
 * The node that represents a whole program
 */
public class Program extends Node implements DataTypeOwner, ConstantListOwner {
    public Program(ConstantList constants, List<CodeBlock> codeBlocks,
                   Map<String, Module> modules, Map<String, DataType> customTypes,
                   Transmission transmission) {
        this.constants = constants;
        if (constants != null)
            constants.setOwner(this);

        this.allBlocks = Collections.unmodifiableList(codeBlocks);
        this.blocksByExecutor = new TreeMap<>();
        this.commonBlocks = new ArrayList<>();
        for (CodeBlock block : codeBlocks) {
            block.setOwner(this);
            List<String> executors = block.getExecutors();
            if (executors == null || executors.isEmpty()) {
                commonBlocks.add(block);
            } else {
                for (String executor : executors) {
                    List<CodeBlock> list = blocksByExecutor.get(executor);
                    if (list != null) {
                        list.add(block);
                    }
                    else {
                        list = new ArrayList<>();
                        list.add(block);
                        blocksByExecutor.put(executor, list);
                    }
                }
            }
        }

        this.moduleMap = Collections.unmodifiableMap(modules);
        this.moduleList = new ArrayList<>();
        for (Map.Entry<String, Module> entry : modules.entrySet()) {
            Module module = entry.getValue();
            module.setOwner(this);
            moduleList.add(module);
        }

        Map<String, DataType> types = new TreeMap<>();
        for (Map.Entry<String, DataType> entry : customTypes.entrySet()) {
            types.put(entry.getKey(), entry.getValue());
            entry.getValue().setOwner(this);
        }

        types.put("BOOLEAN", BooleanType.getInstance());
        types.put("BYTE", ByteType.getInstance());
        types.put("SHORT", ShortType.getInstance());
        types.put("INTEGER", IntegerType.getInstance());
        types.put("LONG", LongType.getInstance());
        types.put("REAL", RealType.getInstance());

        this.types = Collections.unmodifiableMap(types);

        this.transmission = transmission;
        if (transmission != null)
            transmission.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    protected Node[] getChildren() {
        List<Node> list = new ArrayList<>();
        if (constants != null)
            list.add(constants);
        list.addAll(allBlocks);
        for (Map.Entry<String, DataType> entry : types.entrySet()) {
            list.add(entry.getValue());
        }
        list.addAll(moduleList);
        if (transmission != null)
            list.add(transmission);
        Node[] array = new Node[list.size()];
        list.toArray(array);
        return array;
    }

    @Override
    public Node getOwner() {
        return null;
    }

    public List<Module> getModuleList() {
        return moduleList;
    }

    public String toSourceCode() {
        StringBuilder buff = new StringBuilder();
        toSourceCode(buff, "", "\t");
        return buff.toString();
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        boolean flag = false;

        if (constants != null) {
            constants.toSourceCode(buff, i, i0);
            flag = true;
        }

        for (CodeBlock block : allBlocks) {
            if (flag)
                buff.append("\n");
            block.toSourceCode(buff, i, i0);
            flag = true;
        }

        for (Map.Entry<String, DataType> entry : types.entrySet()) {
            DataType type = entry.getValue();
            if (!type.isBuiltIn()) {
                if (flag)
                    buff.append("\n");
                type.toSourceCode(buff, i, i0);
                flag = true;
            }
        }

        for (Module module : moduleList) {
            if (flag)
                buff.append("\n");
            module.toSourceCode(buff, i, i0);
            flag = true;
        }

        if (transmission != null) {
            if (flag)
                buff.append("\n");
            transmission.toSourceCode(buff, i, i0);
            //flag = true;
        }
    }

    Module getModuleByName(String name) {
        return moduleMap.get(name);
    }

    @Override
    protected DataType findTypeByName(String name) {
        if (types.containsKey(name))
            return types.get(name);
        return null;
    }

    @Override
    protected Expression findVariableByName(String name) {
        if (constants != null)
            return constants.findConstantByName(name);
        return null;
    }

    @Override
    protected BaseFunction findFunctionByName(String name) {
        return null;
    }

    public List<CodeBlock> getCodeBlocksByExecutor(String name) {
        List<CodeBlock> result = new ArrayList<>();
        List<CodeBlock> selected = blocksByExecutor.get(name);
        if (selected != null)
            result.addAll(selected);
        result.addAll(commonBlocks);
        return result;
    }

    /**
     * Move all functions and constants from code blocks to modules
     */
    void mergeModulesAndCodeBlocks() throws SyntaxError {
        for (Module module : moduleList) {
            module.mergeCode();
        }
    }

    public CompiledProgram compile() throws SyntaxError {
        List<CompiledModule> binaries = new ArrayList<>();
        for (Module module : moduleList) {
            binaries.add(module.compile());
        }

        return new CompiledProgram(binaries, transmission != null ? transmission.getMapping() : null);
    }

    private ConstantList constants;
    private List<CodeBlock> allBlocks;
    private Map<String, List<CodeBlock>> blocksByExecutor;
    private List<CodeBlock> commonBlocks;
    private List<Module> moduleList;
    private Map<String, Module> moduleMap;
    private Map<String, DataType> types;
    private Transmission transmission;
}
