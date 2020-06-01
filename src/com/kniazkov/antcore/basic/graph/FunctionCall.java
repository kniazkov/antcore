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

import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.bytecodebuilder.Pop;
import com.kniazkov.antcore.basic.bytecodebuilder.PushZeros;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.exceptions.*;

import java.util.*;

/**
 * The function (method) call
 */
public class FunctionCall extends Expression implements ExpressionOwner {
    public FunctionCall(String functionName, List<Expression> arguments) {
        this.functionName = functionName;
        this.arguments = Collections.unmodifiableList(arguments);
        for (Expression argument : arguments) {
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

    @Override
    public DataType getType() {
        return function.getReturnType();
    }

    @Override
    public Object calculate() {
        return null;
    }

    @Override
    public void toDeclarationSourceCode(StringBuilder buff, String i) {
        toUsageSourceCode(buff);
    }

    @Override
    public void toUsageSourceCode(StringBuilder buff) {
        buff.append(functionName).append('(');
        boolean flag = false;
        for (Expression argument : arguments) {
            if (flag)
                buff.append(", ");
            argument.toUsageSourceCode(buff);
            flag = true;
        }
        buff.append(')');
    }

    /**
     * Bind function by name
     */
    void bindName() throws SyntaxError {
        function = findFunctionByName(functionName);
        if (function == null)
            throw new UnknownFunction(getFragment(), functionName);
    }

    void checkArguments() throws SyntaxError {
        List<DataType> expectedArgumentTypes = function.getArgumentTypes();
        int expectedNumberOfArguments = expectedArgumentTypes != null ? expectedArgumentTypes.size() : 0;
        int actualNumberOfArguments = arguments.size();
        if (expectedNumberOfArguments != actualNumberOfArguments)
            throw new InvalidNumberOfArguments(getFragment(), functionName, expectedNumberOfArguments,
                    actualNumberOfArguments);
        if (expectedNumberOfArguments != 0) {
            List<Expression> transformedArguments = new ArrayList<>(actualNumberOfArguments);
            Iterator<DataType> typeIterator = expectedArgumentTypes.iterator();
            Iterator<Expression> argumentsIterator = arguments.iterator();
            for (int k = 0; k < expectedNumberOfArguments; k++) {
                DataType expectedType = typeIterator.next();
                DataType pureExpectedType = expectedType.getPureType();
                Expression argument = argumentsIterator.next();
                DataType actualType = argument.getType();
                DataType pureActualType = actualType.getPureType();
                if (!pureExpectedType.isBinaryAnalog(pureActualType)) {
                    Expression cast = pureExpectedType.dynamicCast(argument, pureActualType);
                    if (cast == null)
                        throw new IncompatibleArgumentType(getFragment(), functionName, k + 1,
                                expectedType.getName(), actualType.getName());
                    cast.setOwner(this);
                    transformedArguments.add(cast);
                }
                else {
                    transformedArguments.add(argument);
                }
            }
            arguments = Collections.unmodifiableList(transformedArguments);
        }
    }

    @Override
    public void genLoad(CompilationUnit unit) throws SyntaxError {
        int popSize = 0;

        // reserve place for return value
        DataType returnType = function.getReturnType();
        if (returnType != null)
            unit.addInstruction(new PushZeros(returnType.getSize()));

        // place arguments to the stack
        if (arguments.size() > 0) {
            ListIterator<Expression> iterator = arguments.listIterator(arguments.size());
            while(iterator.hasPrevious()) {
                Expression argument = iterator.previous();
                argument.genLoad(unit);
                popSize += argument.getType().getSize();
            }
        }

        // call
        function.genCall(unit);

        // remove arguments from the stack
        if (popSize > 0) {
            unit.addInstruction(new Pop(popSize));
        }

        // the return value, if one exists, will remain on the stack
    }

    private String functionName;
    private BaseFunction function;
    private List<Expression> arguments;
}
