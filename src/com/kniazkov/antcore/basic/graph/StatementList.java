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
 * The list of statements
 */
public class StatementList extends Node {
    public StatementList(List<Statement> statements) {
        this.statements = Collections.unmodifiableList(statements);
        for (Statement statement : statements) {
            statement.setOwner(this);
        }
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        for (Statement statement : statements) {
            statement.dfs(visitor);
        }
        accept(visitor);
    }

    void setOwner(StatementListOwner owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        for (Statement statement : statements) {
            statement.toSourceCode(buff, i, i0);
        }
    }

    private StatementListOwner owner;
    private List<Statement> statements;
}
