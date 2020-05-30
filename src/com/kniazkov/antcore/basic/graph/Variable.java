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

import com.kniazkov.antcore.basic.bytecode.DataSelector;
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.bytecodebuilder.Load;
import com.kniazkov.antcore.basic.bytecodebuilder.RawInstruction;
import com.kniazkov.antcore.basic.bytecodebuilder.Store;
import com.kniazkov.antcore.basic.common.*;
import com.kniazkov.antcore.basic.exceptions.IncompatibleTypes;

/**
 * The node represents a variable
 */
public class Variable extends LeftExpression implements DataTypeOwner, ExpressionOwner {
    public Variable(String name, Expression initValue, DataType type) {
        this.name = name;
        assert(initValue != null || type != null);
        this.initValue = initValue;
        if (initValue != null)
            initValue.setOwner(this);
        this.type = type;
        if (type != null)
            type.setOwner(this);
        offset = new DeferredOffset();
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        if (initValue != null && type != null)
            return new Node[] { initValue, type };
        return  new Node[] { initValue !=  null ? initValue : type };
    }

    public String getName() {
        return name;
    }

    public Expression getInitValue() {
        return initValue;
    }

    void setOwner(VariableDeclaration owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return owner;
    }
    
    @Override
    public DataType getType() {
        return type != null ? type : initValue.getType();
    }

    @Override
    public Object calculate() {
        return null;
    }

    @Override
    public void toDeclarationSourceCode(StringBuilder buff, String i) {
        buff.append(name);
        if (initValue != null) {
            buff.append(" = ");
            initValue.toUsageSourceCode(buff);
        }
        if (type != null) {
            buff.append(" AS ").append(type.getName());
        }
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
            type = initValue.getType().getPureType();
        }
        else if (initValue != null) {
            DataType expectedType = type.getPureType();
            DataType actualType = initValue.getType().getPureType();
            if (!expectedType.isBinaryAnalog(actualType)) {
                Expression cast = expectedType.dynamicCast(initValue, actualType);
                if (cast == null)
                    throw new IncompatibleTypes(getFragment(), initValue.getType().getName(), type.getName());
                initValue = cast;
                cast.setOwner(this);
            }
        }
    }

    void setOffset(int value) {
        offset.resolve(value);
    }

    @Override
    public void genLoad(CompilationUnit cu) throws SyntaxError {
        RawInstruction load = new Load(DataSelector.LOCAL,
                type.getSize(), ZeroOffset.getInstance(), offset);
        cu.addInstruction(load);
    }

    @Override
    public void genStore(CompilationUnit cu) throws SyntaxError {
        RawInstruction store = new Store(DataSelector.LOCAL,
                type.getSize(), ZeroOffset.getInstance(), offset);
        cu.addInstruction(store);
    }

    @Override
    public Expression getPointer() {
        return new LocalPointer(this, offset);
    }

    private VariableDeclaration owner;
    private String name;
    private Expression initValue;
    private DataType type;
    private DeferredOffset offset;
}
