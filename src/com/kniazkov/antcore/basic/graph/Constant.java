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
import com.kniazkov.antcore.lib.Variant;

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
    protected Node[] getChildren() {
        if (type != null)
            return new Node[] { value, type };
        return new Node[] { value };
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
    public DataType getType() throws SyntaxError {
        return type != null ? type : value.getType();
    }

    @Override
    public Variant calculate() {
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
            DataType expectedType = type.getPureType();
            DataType actualType = value.getType().getPureType();
            if (!expectedType.isBinaryAnalog(actualType)) {
                Expression cast = expectedType.staticCast(value, actualType);
                if (cast == null)
                    throw new IncompatibleTypes(getFragment(), value.getType().getName(), type.getName());
                value = cast;
                cast.setOwner(this);
            }
        }
    }

    @Override
    public Expression getPointer() throws SyntaxError {
        return value.getPointer();
    }

    @Override
    public void genLoad(CompilationUnit unit) throws SyntaxError {
        value.genLoad(unit);
    }

    @Override
    public Expression[] getExpressions() {
        return new Expression[] {value};
    }

    @Override
    public void replaceExpressions(Expression[] list) {
        assert (list.length == 1);
        value = list[0];
        value.setOwner(this);
    }

    private ConstantList owner;
    private Fragment fragment;
    private String name;
    private Expression value;
    private DataType type;
}
