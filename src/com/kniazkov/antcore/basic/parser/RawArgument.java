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
import com.kniazkov.antcore.basic.graph.Argument;
import com.kniazkov.antcore.basic.graph.Field;

/**
 * The parsed argument of a function
 */
public class RawArgument extends Entity {
    public RawArgument(String name, RawDataType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Argument toNode() {
        return new Argument(name, type.toNode());
    }

    private String name;
    private RawDataType type;
}
