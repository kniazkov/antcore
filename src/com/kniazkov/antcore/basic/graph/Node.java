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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * @return a list of child nodes
     */
    protected abstract Node[] getChildren();

    /**
     * Recursively collect all nodes of the syntax tree into list
     * @param list destination list
     * @param set temporary set of nodes to exclude visited nodes
     */
    protected void enumerate(List<Node> list, Set<Node> set) {
        if (set.contains(this))
            return;
        set.add(this);
        Node[] children = getChildren();
        if (children != null) {
            for (Node child : children)
                child.enumerate(list, set);
        }
        list.add(this);
    }

    /**
     * Collect all nodes of the syntax tree into list
     * @return a list of nodes
     */
    public List<Node> enumerate() {
        List<Node> list = new ArrayList<>();
        Set<Node> set = new HashSet<>();
        enumerate(list, set);
        return list;
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
     * Finds a function by its name
     * @param name the name
     * @return a function or null if not found
     */
    protected BaseFunction findFunctionByName(String name) {
        return getOwner().findFunctionByName(name);
    }

    /**
     * Generate program source code from the node
     * @param buff destination buffer
     * @param i current indentation
     * @param i0 basic indentation
     */
    public abstract void toSourceCode(StringBuilder buff, String i, String i0);
}
