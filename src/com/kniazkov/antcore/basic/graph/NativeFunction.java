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

import java.util.Collections;
import java.util.List;

/**
 * The native function, i.e. function executed by virtual machine
 */
public class NativeFunction extends BaseFunction implements DataTypeOwner {
    public NativeFunction(Fragment fragment, String name, List<DataType> arguments, DataType returnType) {
        super(fragment);
        this.name = name;
        if (arguments != null) {
            this.arguments = Collections.unmodifiableList(arguments);
            for (DataType type : arguments) {
                type.setOwner(this);
            }
        }
        this.returnType = returnType;
        if (returnType != null)
            returnType.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        if (arguments != null) {
            for (DataType type : arguments) {
                type.dfs(visitor);
            }
        }
        if (returnType != null)
            returnType.dfs(visitor);
        accept(visitor);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<DataType> getArgumentTypes() {
        return arguments;
    }

    @Override
    public DataType getReturnType() {
        return returnType;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append("DECLARE FUNCTION ").append(name);
        if (arguments != null) {
            buff.append('(');
            boolean flag = false;
            for (DataType type : arguments) {
                if (flag)
                    buff.append(", ");
                buff.append(type.getName());
                flag = true;
            }
            buff.append(')');
        }
        if (returnType != null)
            buff.append(" AS ").append(returnType.getName());
        buff.append('\n');
    }

    private String name;
    private List<DataType> arguments;
    private DataType returnType;
}
