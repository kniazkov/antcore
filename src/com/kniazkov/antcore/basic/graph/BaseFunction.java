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
import com.kniazkov.antcore.basic.common.Fragment;

import java.util.List;

/**
 * Base class for functions
 */
public abstract class BaseFunction extends Node {
    public BaseFunction(Fragment fragment) {
        this.fragment = fragment;
    }

    void setOwner(FunctionOwner owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }

    /**
     * @return name of the function
     */
    public abstract String getName();

    /**
     * @return a list of types of arguments
     */
    public abstract List<DataType> getArgumentTypes();

    /**
     * @return a type of returned value
     */
    public abstract DataType getReturnType();

    /**
     * Generate instruction(s) for calling this function
     * @param unit the compilation unit
     */
    public abstract void genCall(CompilationUnit unit);

    private FunctionOwner owner;
    private Fragment fragment;
}
