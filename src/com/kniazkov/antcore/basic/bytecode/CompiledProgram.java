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

import java.util.*;

/**
 * The whole compiled program
 */
public class CompiledProgram {
    public CompiledProgram(List<CompiledModule> binaries, List<Binding> mapping) {
        this.binaries = Collections.unmodifiableList(binaries);
        this.mapping = Collections.unmodifiableList(mapping);

        init(binaries, mapping);
    }

    private void init(List<CompiledModule> binaries, List<Binding> mapping) {
        modules = new TreeMap<>();
        executors = new ArrayList<>();
        for (CompiledModule module : binaries) {
            String executor  = module.getExecutor();
            List<CompiledModule> sublist = modules.get(executor);
            if (sublist != null) {
                sublist.add(module);
            }
            else {
                sublist = new ArrayList<>();
                sublist.add(module);
                modules.put(executor, sublist);
                executors.add(executor);
            }
        }
    }

    public String[] getExecutors() {
        String[] result = new String[executors.size()];
        executors.toArray(result);
        return result;
    }

    public CompiledModule[] getModulesByExecutor(String name) {
        List<CompiledModule> list = modules.get(name);
        if (list == null)
            return new CompiledModule[0];
        CompiledModule[] result = new CompiledModule[list.size()];
        list.toArray(result);
        return result;
    }

    private List<CompiledModule> binaries;
    private List<Binding> mapping;
    private Map<String, List<CompiledModule>> modules;
    private List<String> executors;
}
