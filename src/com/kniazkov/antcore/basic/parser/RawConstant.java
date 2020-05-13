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

import com.kniazkov.antcore.basic.Fragment;
import com.kniazkov.antcore.basic.graph.Constant;
import com.kniazkov.antcore.basic.parser.tokens.TokenExpression;

/**
 * The parsed constant
 */
public class RawConstant extends Entity {
    public RawConstant(Fragment fragment, String name, TokenExpression value, RawDataType type) {
        this.fragment = fragment;
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public Constant toNode() {
        return new Constant(fragment, name, value.toNode(), type.toNode());
    }

    private Fragment fragment;
    private String name;
    private TokenExpression value;
    private RawDataType type;
}
