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
import com.kniazkov.antcore.basic.bytecode.CompilationUnit;

/**
 * The node represents an function (method) argument
 */
public class Argument extends LeftExpression implements DataTypeOwner {
    public Argument(String name, DataType type) {
        this.name = name;
        this.type = type;
        type.setOwner(this);
        this.offset = -1;
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        type.dfs(visitor);
        accept(visitor);
    }

    public String getName() {
        return name;
    }

    @Override
    void setOwner(ExpressionOwner owner) {
        this.owner = (ArgumentList) owner;
    }

    void setOwner(ArgumentList owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return owner;
    }
    
    @Override
    public DataType getType() {
        return type;
    }

    @Override
    public void toDeclarationSourceCode(StringBuilder buff, String i) {
        buff.append(name).append(" AS ").append(type.getName());
    }

    @Override
    public void toUsageSourceCode(StringBuilder buff) {
        buff.append(name);
    }

    public int getOffset() {
        return offset;
    }

    void setOffset(int offset) {
        assert(offset == -1);
        this.offset = offset;
    }

    @Override
    public void load(CompilationUnit cu) {
        assert(false);
    }

    @Override
    public void store(CompilationUnit cu) throws SyntaxError {
        assert(false);
    }

    private ArgumentList owner;
    private String name;
    private DataType type;
    private int offset;
}
