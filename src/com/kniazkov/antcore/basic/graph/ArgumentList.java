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

import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.exceptions.ArgumentCanNotBeAbstract;
import com.kniazkov.antcore.basic.exceptions.ArgumentCanNotBeConstant;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The list of arguments
 */
public class ArgumentList extends Node implements ExpressionOwner {
    public ArgumentList(List<Argument> arguments) {
        this.arguments = Collections.unmodifiableList(arguments);
        for (Argument argument : arguments) {
            argument.setOwner(this);
        }
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        Node[] nodes = new Node[arguments.size()];
        arguments.toArray(nodes);
        return nodes;
    }

    void setOwner(Function owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return owner;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append('(');
        boolean flag = false;
        for (Argument argument : arguments) {
            if (flag)
                buff.append(", ");
            flag = true;
            argument.toSourceCode(buff, i, i0);
        }
        buff.append(')');
    }

    public int getCount() {
        return arguments.size();
    }

    /**
     * Calculate offsets of all arguments
     */
    void calculateOffsets() throws SyntaxError {
        int offset = 0;
        for (Argument argument : arguments) {
            argument.setOffset(offset);
            offset += argument.getType().getSize();
        }
    }

    /**
     * Check types of all arguments
     */
    void checkTypes() throws SyntaxError {
        for (Argument argument : arguments) {
            DataType type = argument.getType();
            if (type.isConstant())
                throw new ArgumentCanNotBeConstant(getFragment());
            if (type.isAbstract())
                throw new ArgumentCanNotBeAbstract(getFragment());
        }
    }

    /**
     * @return list contains types of each argument
     */
    public List<DataType> getTypes() {
        if (types == null) {
            ArrayList<DataType> list = new ArrayList<>(arguments.size());
            for (Argument argument : arguments) {
                list.add(argument.getType());
            }
            types = Collections.unmodifiableList(list);
        }
        return types;
    }

    private Function owner;
    private List<Argument> arguments;
    private List<DataType> types;
}
