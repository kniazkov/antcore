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

import com.kniazkov.antcore.basic.Fragment;
import com.kniazkov.antcore.basic.SyntaxError;

/**
 * The exception "Incompatible types"
 */
public class IncompatibleTypes extends SyntaxError {
    public IncompatibleTypes(Fragment fragment, String leftType, String rightType) {
        super(fragment);
        this.leftType = leftType;
        this.rightType = rightType;
    }

    @Override
    protected String getErrorMessage() {
        return "Incompatible types, '" + leftType + "' cannot be converted to '" + rightType + '\'';
    }

    private String leftType;
    private String rightType;
}
