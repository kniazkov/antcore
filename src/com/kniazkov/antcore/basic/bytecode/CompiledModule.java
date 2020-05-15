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
package com.kniazkov.antcore.basic.bytecode;

import com.kniazkov.antcore.lib.ByteList;

/**
 * The compiled module
 */
public class CompiledModule {
    public CompiledModule(String executor, String name, ByteList bytecode) {
        this.executor = executor;
        this.name = name;
        this.bytecode = bytecode;
    }

    public String getExecutor() {
        return executor;
    }

    public String  getName() {
        return name;
    }

    public ByteList getBytecode() {
        return bytecode;
    }

    private String executor;
    private String name;
    private ByteList bytecode;
}
