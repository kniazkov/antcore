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

/**
 * The basic interface for node of syntax tree
 */
public abstract class Node {
    /**
     * Accept a visitor
     * @param visitor a visitor
     */
    public void accept (NodeVisitor visitor) throws SyntaxError {
    }

    /**
     * Depth-first graph traversal
     * @param visitor a visitor
     */
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        accept(visitor);
    }

    /**
     * @return node contains this node
     */
    public abstract Node getOwner();

    /**
     * @return fragment contains this node
     */
    public Fragment getFragment() {
        return getOwner().getFragment();
    }

    /**
     * Finds a data type by its name
     * @param name the name
     * @return a type or null if not found
     */
    protected DataType findTypeByName(String name) {
        return getOwner().findTypeByName(name);
    }

    /**
     * Finds a variable by its name
     * @param name the name
     * @return a variable or null if not found
     */
    protected Expression findVariableByName(String name) {
        return getOwner().findVariableByName(name);
    }

    /**
     * Generate program source code from the node
     * @param buff destination buffer
     * @param i current indentation
     * @param i0 basic indentation
     */
    public abstract void toSourceCode(StringBuilder buff, String i, String i0);
}
