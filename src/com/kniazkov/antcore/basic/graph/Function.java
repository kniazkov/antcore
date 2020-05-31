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

import com.kniazkov.antcore.basic.bytecodebuilder.Enter;
import com.kniazkov.antcore.basic.bytecodebuilder.Leave;
import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.bytecodebuilder.Return;
import com.kniazkov.antcore.basic.exceptions.ReturnTypeCanNotBeAbstract;
import com.kniazkov.antcore.basic.exceptions.ReturnTypeCanNotBeConstant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The function
 */
public class Function extends BaseFunction implements DataTypeOwner, StatementListOwner {
    public Function(Fragment fragment, String name, ArgumentList arguments, DataType returnType, StatementList body) {
        super(fragment);
        this.name = name;
        this.arguments = arguments;
        if (arguments != null)
            arguments.setOwner(this);
        this.returnType = returnType;
        if (returnType != null)
            returnType.setOwner(this);
        this.body = body;
        body.setOwner(this);
        variableList = new ArrayList<>();
        localDataSize = 0;
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        if (arguments != null && returnType != null)
            return new Node[] { arguments, returnType, body };
        if (arguments != null)
            return new Node[] { arguments, body };
        if (returnType != null)
            return new Node[] { returnType, body };
        return new Node[] { body };
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append("FUNCTION ").append(name);
        if (arguments != null)
            arguments.toSourceCode(buff, i, i0);
        if (returnType != null) {
            buff.append(" AS ");
            returnType.toSourceCode(buff, i, i0);
        }
        buff.append('\n');
        String i1 = i + i0;
        body.toSourceCode(buff, i1, i0);
        buff.append(i).append("END FUNCTION\n");
    }

    public int getArgumentsCount() {
        return arguments != null ? arguments.getCount() : 0;
    }

    @Override
    public DataType getReturnType() {
        return returnType;
    }

    @Override
    public void genCall(CompilationUnit cu) {
        assert(false);
    }

    @Override
    public List<DataType> getArgumentTypes() {
        return arguments != null ? arguments.getTypes() : null;
    }

    public void compile(CompilationUnit cu) throws SyntaxError {
        assert (localDataSize >= 0);
        cu.addInstruction(new Enter(localDataSize));
        body.compile(cu);
        cu.addInstruction(new Leave(localDataSize));
        cu.addInstruction(new Return());
    }

    /**
     * Check correctness of return type
     */
    void checkReturnType() throws SyntaxError {
        if (returnType != null) {
            if (returnType.isConstant())
                throw new ReturnTypeCanNotBeConstant(getFragment());
            if (returnType.isAbstract())
                throw new ReturnTypeCanNotBeAbstract(getFragment());
        }
    }

    @Override
    public Function getFunction() {
        return this;
    }

    /**
     * Create a variable
     * @param name the name of a variable
     * @param type the type of a variable
     * @return a variable
     */
    public Variable createVariable(String name, DataType type) throws SyntaxError {
        Variable variable = new Variable(name, null, type);
        int size = type.getSize();
        localDataSize += size;
        variable.setOffset(-localDataSize);
        variableList.add(variable);
        return variable;
    }

    /**
     * Create a temporary variable for data conversion
     * @param type the type of a variable
     * @return a variable
     */
    public Variable createTemporaryVariable(DataType type) throws SyntaxError {
        Variable variable = new Variable("$", null, type);
        int size = type.getSize();
        localDataSize += size;
        variable.setOffset(-localDataSize);
        variableList.add(variable);
        return variable;
    }

    /**
     * Go through all the statement lists and collect the variables,
     * then calculate offsets
     */
    void collectVariablesAndCalculateOffsets() throws SyntaxError {
        class Collector extends NodeVisitor {
            List<Variable> result;

            Collector() {
                result = new ArrayList<>();
            }

            @Override
            public void visit(StatementList obj)  throws SyntaxError {
                obj.collectVariables(result);
            }
        }

        Collector collector = new Collector();
        List<Node> children = body.enumerate();
        for (Node child : children) {
            child.accept(collector);
        }
        for  (Variable variable : collector.result) {
            int size = variable.getType().getSize();
            localDataSize += size ;
            variable.setOffset(-localDataSize);
            variableList.add(variable);
        }
    }

    private String name;
    private ArgumentList arguments;
    private DataType returnType;
    private StatementList body;
    private List<Variable> variableList;
    private int localDataSize;
}
