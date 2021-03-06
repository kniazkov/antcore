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
package com.kniazkov.antcore.basic.parser;

import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.graph.Else;
import com.kniazkov.antcore.basic.graph.Statement;
import com.kniazkov.antcore.basic.graph.StatementList;
import com.kniazkov.antcore.basic.parser.tokens.TokenExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * A parsed ELSE statement
 */
public class RawElse extends Entity {
    public RawElse(Fragment fragment, List<RawStatement> rawStatements) {
        this.fragment = fragment;
        this.rawStatements = rawStatements;
    }

    public Else toNode() {
        List<Statement> body = new ArrayList<>();
        for (RawStatement rawStatement : rawStatements) {
            Statement statement = rawStatement.toNode();
            body.add(statement);
        }

        return new Else(fragment, new StatementList(body));
    }

    private Fragment fragment;
    private List<RawStatement> rawStatements;
}
