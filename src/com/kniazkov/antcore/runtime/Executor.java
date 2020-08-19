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
package com.kniazkov.antcore.runtime;

import com.kniazkov.antcore.basic.bytecode.Binding;
import com.kniazkov.antcore.basic.bytecode.CompiledModule;
import com.kniazkov.antcore.basic.bytecode.ShortAddress;
import com.kniazkov.antcore.lib.Periodic;

/**
 * The class that contains data of executing modules
 */
public abstract class Executor extends Periodic {
    public Executor(Runtime runtime) {
        this.runtime = runtime;
    }

    /**
     * @return name of executor
     */
    public abstract String getName();

    /**
     * Sets module list for execution
     * @param modules a list
     */
    public abstract void setModuleList(CompiledModule[] modules);

    /**
     * Sets binding list
     * @param bindingByModule a list
     */
    public abstract void setBindingByModule(Binding[] bindingByModule);

    /**
     * Reads data from module to buffer by full absolute address
     * @param address short absolute address
     * @param size size of the data
     * @param buffer destination buffer
     * @return true if the operation was successful
     */
    public abstract boolean read(ShortAddress address, int size, byte[] buffer);

    /**
     * Initializer
     */
    protected abstract void init();

    /**
     * @return how many time per second the 'tick' method called
     */
    protected abstract int getFrequency();

    /**
     * Runs the executor
     */
    public void run() {
        init();
        start(1000 / getFrequency());
    }

    public Runtime getRuntime() {
        return runtime;
    }

    private Runtime runtime;
}
