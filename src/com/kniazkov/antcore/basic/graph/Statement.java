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

import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;

/**
 * The node represents statement, i.e. an instruction
 */
public abstract class Statement extends Node {
    public Statement(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public Node getOwner() {
        return owner;
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }

    void setOwner(StatementList owner) {
        this.owner = owner;
    }

    /**
     * Generate instructions
     * @param unit the compilation unit
     */
    public abstract void compile(CompilationUnit unit) throws SyntaxError;

    /**
     * Get a function containing this statement
     * @return function
     */
    public Function getFunction() {
        return owner.getFunction();
    }

    private StatementList owner;
    private Fragment fragment;
}
