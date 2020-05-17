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

import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.exceptions.DuplicateConstant;
import com.kniazkov.antcore.basic.graph.Constant;
import com.kniazkov.antcore.basic.graph.ConstantList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The parsed list of constants
 */
public class RawConstantList extends Entity {
    RawConstantList() {
        rawList = new ArrayList<>();
        rawMap = new TreeMap<>();
    }

    public void addConstant(RawConstant constant) throws SyntaxError {
        String name = constant.getName();
        if (rawMap.containsKey(name))
            throw new DuplicateConstant(constant.getFragment(), name);
        rawList.add(constant);
        rawMap.put(name, constant);
    }

    public ConstantList toNode() {
        List<Constant> constants = new ArrayList<>();
        for (RawConstant rawConstant : rawList) {
            constants.add(rawConstant.toNode());
        }
        return new ConstantList(constants);
    }

    private List<RawConstant> rawList;
    private Map<String, RawConstant> rawMap;
}
