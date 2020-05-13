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

import com.kniazkov.antcore.basic.SyntaxError;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * The node that represents a whole program
 */
public class Program extends Node implements DataTypeOwner, ConstantListOwner {
    public Program(ConstantList constants, Map<String, Module> modules, Map<String, DataType> customTypes) {
        this.constants = constants;
        if (constants != null)
            constants.setOwner(this);

        this.modules = Collections.unmodifiableMap(modules);
        for (Map.Entry<String, Module> entry : modules.entrySet()) {
            entry.getValue().setOwner(this);
        }


        Map<String, DataType> types = new TreeMap<>();
        for (Map.Entry<String, DataType> entry : customTypes.entrySet()) {
            types.put(entry.getKey(), entry.getValue());
            entry.getValue().setOwner(this);
        }

        types.put("INTEGER", IntegerType.getInstance());
        types.put("REAL", RealType.getInstance());

        this.types = Collections.unmodifiableMap(types);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        if (constants != null)
            constants.dfs(visitor);
        for (Map.Entry<String, DataType> entry : types.entrySet()) {
            entry.getValue().dfs(visitor);
        }
        for (Map.Entry<String, Module> entry : modules.entrySet()) {
            entry.getValue().dfs(visitor);
        }
        accept(visitor);
    }

    public String toSourceCode() {
        StringBuilder buff = new StringBuilder();
        toSourceCode(buff, "", "\t");
        return buff.toString();
    }

    @Override
    public Node getOwner() {
        return null;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        boolean flag = false;

        if (constants != null) {
            constants.toSourceCode(buff, i, i0);
            flag = true;
        }

        for (Map.Entry<String, DataType> entry : types.entrySet()) {
            DataType type = entry.getValue();
            if (!type.builtIn()) {
                if (flag)
                    buff.append("\n");
                type.toSourceCode(buff, i, i0);
                flag = true;
            }
        }

        for (Map.Entry<String, Module> entry : modules.entrySet()) {
            if (flag)
                buff.append("\n");
            entry.getValue().toSourceCode(buff, i, i0);
            flag = true;
        }
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

    private ConstantList constants;
    private Map<String, Module> modules;
    private Map<String, DataType> types;
}
