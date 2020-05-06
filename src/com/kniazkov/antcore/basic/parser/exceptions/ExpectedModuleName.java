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
package com.kniazkov.antcore.basic.parser.exceptions;

import com.kniazkov.antcore.basic.parser.Line;
import com.kniazkov.antcore.basic.parser.ParseError;

/**
 * The exception "Expected a module name"
 */
public class ExpectedModuleName extends ParseError {
    public ExpectedModuleName(Line line) {
        super(line);
    }

    @Override
    protected String getErrorMessage() {
        return "Expected a module name";
    }
}
