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

import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.parser.Line;

/**
 * The exception "Counter variable does not match declared"
 */
public class CounterDoesNotMatch extends SyntaxError {
    public CounterDoesNotMatch(Line line, String expected, String actual) {
        super(line.getFragment());
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    protected String getErrorMessage() {
        return "The counter variable '" + actual + "' does not match declared '" + expected + '\'';
    }

    private String expected;
    private String actual;
}
