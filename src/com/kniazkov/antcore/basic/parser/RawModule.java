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

import com.kniazkov.antcore.basic.DataPrefix;
import com.kniazkov.antcore.basic.Fragment;
import com.kniazkov.antcore.basic.SyntaxError;
import com.kniazkov.antcore.basic.graph.*;
import com.kniazkov.antcore.basic.parser.exceptions.DuplicateDataSet;
import com.kniazkov.antcore.basic.parser.exceptions.FunctionAlreadyExists;
import com.kniazkov.antcore.basic.parser.exceptions.UnexpectedDataSet;
import com.kniazkov.antcore.lib.Pair;

import java.util.*;

/**
 * Entity represents module
 */
public class RawModule extends Entity {
    public RawModule(Fragment fragment, String name, String executor, List<Line> body) {
        this.fragment = fragment;
        this.name = name;
        this.executor = executor;
        this.body = Collections.unmodifiableList(body);
        this.rawFunctions = new TreeMap<>();
    }

    public List<Line> getBody() {
        return body;
    }

    public void setData(RawDataSet dataSet) throws SyntaxError {
        DataPrefix prefix = dataSet.getPrefix();
        switch (prefix) {
            case PRIVATE:
                if (localData != null)
                    throw new DuplicateDataSet(dataSet.getFragment(), prefix);
                localData = dataSet;
                break;
            case INPUT:
                if (inputData != null)
                    throw new DuplicateDataSet(dataSet.getFragment(), prefix);
                inputData = dataSet;
                break;
            case OUTPUT:
                if (outputData != null)
                    throw new DuplicateDataSet(dataSet.getFragment(), prefix);
                outputData = dataSet;
                break;
            default:
                throw new UnexpectedDataSet(dataSet.getFragment(), prefix);
        }
    }

    public void addFunction(RawFunction function) throws SyntaxError {
        String name = function.getName();
        if (rawFunctions.containsKey(name))
            throw new FunctionAlreadyExists(function.getFragment(), name);
        rawFunctions.put(name, function);
    }

    public Module toNode() {
        DataSet localDataNode = localData != null ? localData.toNode() : null;
        DataSet inputDataNode = inputData != null ? inputData.toNode() : null;
        DataSet outputDataNode = outputData != null ? outputData.toNode() : null;
        List<Function> functions = new ArrayList<>();
        for (Map.Entry<String, RawFunction> entry : rawFunctions.entrySet()) {
            Function function = entry.getValue().toNode();
            functions.add(function);
        }

        return new Module(fragment, name, executor,
                localDataNode, inputDataNode, outputDataNode, functions);
    }

    private Fragment fragment;
    private String name;
    private String executor;
    private List<Line> body;
    private RawDataSet localData;
    private RawDataSet inputData;
    private RawDataSet outputData;
    private Map<String, RawFunction> rawFunctions;
}
