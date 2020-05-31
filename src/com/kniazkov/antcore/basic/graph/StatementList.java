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
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;

import java.util.*;

/**
 * The list of statements
 */
public class StatementList extends Node {
    public StatementList(List<Statement> statements) {
        this.statements = Collections.unmodifiableList(statements);
        variableList = new ArrayList<>();
        variableMap = new TreeMap<>();
        for (Statement statement : statements) {
            statement.setOwner(this);
            if (statement instanceof VariableDeclaration) {
                Variable variable = ((VariableDeclaration) statement).getVariable();
                variableList.add(variable);
                variableMap.put(variable.getName(), variable);
            }
        }
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        Node[] nodes = new Node[statements.size()];
        statements.toArray(nodes);
        return nodes;
    }

    void setOwner(StatementListOwner owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    public void addVariable(Variable variable) {
        String name = variable.getName();
        assert (!variableMap.containsKey(name));
        variableMap.put(variable.getName(), variable);
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        for (Statement statement : statements) {
            statement.toSourceCode(buff, i, i0);
        }
    }

    public void compile(CompilationUnit cu) throws SyntaxError {
        for (Statement statement : statements) {
            statement.compile(cu);
        }
    }

    /**
     * Collects variables into one list
     * @param dst the destination list
     */
    void collectVariables(List<Variable> dst) {
        dst.addAll(variableList);
    }

    @Override
    protected Expression findVariableByName(String name) {
        Variable variableOfThisScope = variableMap.get(name);
        if (variableOfThisScope != null)
            return variableOfThisScope;
        return ((Node)owner).findVariableByName(name);
    }

    /**
     * Get a function containing this statement list
     * @return function
     */
    public Function getFunction() {
        return owner.getFunction();
    }

    private StatementListOwner owner;
    private List<Statement> statements;
    private List<Variable> variableList;
    private Map<String, Variable> variableMap;
}
