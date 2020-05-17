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
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.exceptions.IncompatibleTypes;

/**
 * The node represents a constant
 */
public class Constant extends Expression implements DataTypeOwner, ExpressionOwner {
    public Constant(Fragment fragment, String name, Expression value, DataType type) {
        this.fragment = fragment;
        this.name = name;
        this.value = value;
        value.setOwner(this);
        this.type = type;
        if (type != null)
            type.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        value.dfs(visitor);
        if (type != null)
            type.dfs(visitor);
        accept(visitor);
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }

    public String getName() {
        return name;
    }

    @Override
    void setOwner(ExpressionOwner owner) {
        this.owner = (ConstantList) owner;
    }

    void setOwner(ConstantList owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return owner;
    }
    
    @Override
    public DataType getType() {
        return type != null ? type : value.getType();
    }

    @Override
    public Object calculate() {
        return value.calculate();
    }

    @Override
    public void toDeclarationSourceCode(StringBuilder buff, String i) {
        buff.append(i).append("CONST ").append(name).append(" = ");
        value.toUsageSourceCode(buff);
        if (type != null) {
            buff.append(" AS ").append(type.getName());
        }
        buff.append('\n');
    }

    @Override
    public void toUsageSourceCode(StringBuilder buff) {
        buff.append(name);
    }

    /**
     * Check data type or inference it
     */
    void checkType() throws SyntaxError {
        if (type == null) {
            type = value.getType();
        }
        else {
            if (!value.getType().getPureType().canBeCastTo(type.getPureType()))
                throw new IncompatibleTypes(getFragment(), value.getType().getName(), type.getName());
        }
    }

    @Override
    public void load(CompilationUnit cu) throws SyntaxError {
        value.load(cu);
    }

    private ConstantList owner;
    private Fragment fragment;
    private String name;
    private Expression value;
    private DataType type;
}
