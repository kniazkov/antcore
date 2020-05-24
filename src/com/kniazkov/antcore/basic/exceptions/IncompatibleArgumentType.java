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
package com.kniazkov.antcore.basic.exceptions;

import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;

/**
 * The exception "Incompatible argument type"
 */
public class IncompatibleArgumentType extends SyntaxError {
    public IncompatibleArgumentType(Fragment fragment, String functionName, int argumentNumber,
                                    String expectedType, String actualType) {
        super(fragment);
        this.functionName = functionName;
        this.argumentNumber = argumentNumber;
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    @Override
    protected String getErrorMessage() {
        return "Call of the '" + functionName + "' function, incompatible types for the argument "+ argumentNumber +
                ", '" + actualType + "' cannot be converted to '" + expectedType + '\'';
    }

    private String functionName;
    private int argumentNumber;
    private String expectedType;
    private String actualType;
}
