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

import com.kniazkov.antcore.basic.common.SyntaxError;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The list of constants
 */
public class ConstantList extends Node implements ExpressionOwner {
    public ConstantList(List<Constant> constants) {
        this.constantList = Collections.unmodifiableList(constants);
        this.constantMap = new TreeMap<>();
        for (Constant constant : constants) {
            constant.setOwner(this);
            constantMap.put(constant.getName(), constant);
        }
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        Node[] nodes = new Node[constantList.size()];
        constantList.toArray(nodes);
        return nodes;
    }

    void setOwner(ConstantListOwner owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        for (Constant constant : constantList) {
            constant.toSourceCode(buff, i, i0);
        }
    }

    @Override
    public Expression[] getExpressions() {
        return new Expression[]{};
    }

    @Override
    public void replaceExpressions(Expression[] list) {
        assert (list.length == 0);
    }

    Constant findConstantByName(String name) {
        return constantMap.get(name);
    }

    private ConstantListOwner owner;
    private List<Constant> constantList;
    private Map<String, Constant> constantMap;
}
