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
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.graph.*;
import com.kniazkov.antcore.basic.parser.exceptions.FunctionAlreadyExists;
import com.kniazkov.antcore.basic.parser.exceptions.FunctionCannotBeDeclared;

import java.util.*;

/**
 * Entity represents code block
 */
public class RawCodeBlock extends Entity {
    public RawCodeBlock(Fragment fragment, List<String> executors, List<Line> body) {
        this.fragment = fragment;
        this.executors = executors;
        this.body = Collections.unmodifiableList(body);
        this.rawNativeFunctions = new ArrayList<>();
        this.rawFunctions = new ArrayList<>();
        this.allRawFunctions = new TreeMap<>();
    }

    public List<Line> getBody() {
        return body;
    }

    public void addNativeFunction(RawNativeFunction function) throws SyntaxError {
        String name = function.getName();
        if (name.equals("MAIN"))
            throw new FunctionCannotBeDeclared(function.getFragment(), name);
        if (allRawFunctions.containsKey(name))
            throw new FunctionAlreadyExists(function.getFragment(), name);
        rawNativeFunctions.add(function);
        allRawFunctions.put(name, function);
    }

    public void addFunction(RawFunction function) throws SyntaxError {
        String name = function.getName();
        if (name.equals("MAIN"))
            throw new FunctionCannotBeDeclared(function.getFragment(), name);
        if (allRawFunctions.containsKey(name))
            throw new FunctionAlreadyExists(function.getFragment(), name);
        rawFunctions.add(function);
        allRawFunctions.put(name, function);
    }

    public CodeBlock toNode() {
        List<NativeFunction> nativeFunctions = new ArrayList<>();
        for (RawNativeFunction rawFunction : rawNativeFunctions) {
            NativeFunction function = rawFunction.toNode();
            nativeFunctions.add(function);
        }
        List<Function> functions = new ArrayList<>();
        for (RawFunction rawFunction : rawFunctions) {
            Function function = rawFunction.toNode();
            functions.add(function);
        }
        return new CodeBlock(fragment, executors, nativeFunctions, functions);
    }

    private Fragment fragment;
    private List<String> executors;
    private List<Line> body;
    private List<RawNativeFunction> rawNativeFunctions;
    private List<RawFunction> rawFunctions;
    private Map<String, RawBaseFunction> allRawFunctions;
}
