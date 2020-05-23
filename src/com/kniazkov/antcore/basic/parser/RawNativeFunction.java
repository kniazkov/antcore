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
import com.kniazkov.antcore.basic.graph.DataType;
import com.kniazkov.antcore.basic.graph.NativeFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entity represents native function
 */
public class RawNativeFunction extends RawBaseFunction {
    public RawNativeFunction(Fragment fragment, String name, List<RawDataType> arguments, RawDataType returnType) {
        this.fragment = fragment;
        this.name = name;
        this.rawArguments = arguments != null ? Collections.unmodifiableList(arguments) : null;
        this.returnType = returnType;
    }

    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public String getName() {
        return name;
    }

    public NativeFunction toNode() {
        List<DataType> arguments = null;
        if (rawArguments != null) {
            arguments = new ArrayList<>();
            for (RawDataType rawType : rawArguments) {
                DataType type = rawType.toNode();
                arguments.add(type);
            }
        }
        return new NativeFunction(fragment, name, arguments, returnType != null ? returnType.toNode() : null);
    }

    private Fragment fragment;
    private String name;
    private List<RawDataType> rawArguments;
    private RawDataType returnType;
}
