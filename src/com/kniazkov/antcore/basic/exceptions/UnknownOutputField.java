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
 * The exception "Unknown output field"
 */
public class UnknownOutputField extends SyntaxError {
    public UnknownOutputField(Fragment fragment, String module, String field) {
        super(fragment);
        this.module = module;
        this.field = field;
    }

    @Override
    protected String getErrorMessage() {
        return "Unknown output field \'" + module + '.' + field + "\'";
    }

    private String module;
    private String field;
}
