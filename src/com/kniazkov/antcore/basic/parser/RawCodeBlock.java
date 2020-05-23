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

import com.kniazkov.antcore.basic.common.DataPrefix;
import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.graph.CodeBlock;
import com.kniazkov.antcore.basic.graph.DataSet;
import com.kniazkov.antcore.basic.graph.Function;
import com.kniazkov.antcore.basic.graph.Module;
import com.kniazkov.antcore.basic.parser.exceptions.DuplicateDataSet;
import com.kniazkov.antcore.basic.parser.exceptions.FunctionAlreadyExists;
import com.kniazkov.antcore.basic.parser.exceptions.UnexpectedDataSet;

import java.util.*;

/**
 * Entity represents code block
 */
public class RawCodeBlock extends Entity {
    public RawCodeBlock(Fragment fragment, List<String> executors, List<Line> body) {
        this.fragment = fragment;
        this.executors = executors;
        this.body = Collections.unmodifiableList(body);
    }

    public List<Line> getBody() {
        return body;
    }

    public CodeBlock toNode() {
        return new CodeBlock(fragment, executors);
    }

    private Fragment fragment;
    private List<String> executors;
    private List<Line> body;
}
