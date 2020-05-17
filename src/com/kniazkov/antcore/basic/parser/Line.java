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

import java.util.Collections;
import java.util.List;

/**
 * One line of source code
 */
public class Line {
    public Line(Fragment fragment, List<Token> tokens) {
        this.fragment = fragment;
        this.tokens = Collections.unmodifiableList(tokens);
    }

    public Fragment getFragment() {
        return fragment;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    private Fragment fragment;
    private List<Token> tokens;
}
