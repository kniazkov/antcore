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
import com.kniazkov.antcore.basic.graph.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entity represents function
 */
public class RawFunction extends RawBaseFunction {
    public RawFunction(Fragment fragment, String name, List<RawArgument> arguments,
                       RawDataType returnType, List<Line> body) {
        this.fragment = fragment;
        this.name = name;
        this.rawArguments = arguments != null ? Collections.unmodifiableList(arguments) : null;
        this.returnType = returnType;
        this.rawBody = Collections.unmodifiableList(body);
    }

    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<Line> getRawBody() {
        return rawBody;
    }

    public void setRawStatementList(List<RawStatement> rawStatements) {
        this.rawStatements = rawStatements;
    }

    public Function toNode() {
        List<Argument> arguments = null;
        if (rawArguments != null) {
            arguments = new ArrayList<>();
            for (RawArgument rawArgument : rawArguments) {
                Argument argument = rawArgument.toNode();
                arguments.add(argument);
            }
        }
        List<Statement> body = new ArrayList<>();
        for (RawStatement rawStatement : rawStatements) {
            Statement statement = rawStatement.toNode();
            body.add(statement);
        }
        return new Function(fragment, name,
                arguments != null ? new ArgumentList(arguments) : null,
                returnType != null ? returnType.toNode() : null,
                new StatementList(body));
    }

    private Fragment fragment;
    private String name;
    private List<RawArgument> rawArguments;
    private RawDataType returnType;
    private List<Line> rawBody;
    private List<RawStatement> rawStatements;
}
