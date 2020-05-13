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
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        for (Argument argument : arguments) {
            argument.dfs(visitor);
        }
        accept(visitor);
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

    private Function owner;
    private List<Argument> arguments;
}
