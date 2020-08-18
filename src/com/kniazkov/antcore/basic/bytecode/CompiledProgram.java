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
        executors = new ArrayList<>();
        modulesByExecutor = new TreeMap<>();
        bindingsByDestination = new TreeMap<>();

        for (CompiledModule module : binaries) {
            String executor  = module.getExecutor();
            List<CompiledModule> sublist = modulesByExecutor.get(executor);
            if (sublist != null) {
                sublist.add(module);
            }
            else {
                sublist = new ArrayList<>();
                sublist.add(module);
                modulesByExecutor.put(executor, sublist);
                executors.add(executor);
            }
        }

        for (Binding binding : mapping) {
            String executor = binding.getDestination().getExecutor();
            List<Binding> sublist = bindingsByDestination.get(executor);
            if (sublist != null) {
                sublist.add(binding);
            } else {
                sublist = new ArrayList<>();
                sublist.add(binding);
                bindingsByDestination.put(executor, sublist);
            }
        }
    }

    public String[] getExecutors() {
        String[] result = new String[executors.size()];
        executors.toArray(result);
        return result;
    }

    public CompiledModule[] getModulesByExecutor(String name) {
        List<CompiledModule> list = modulesByExecutor.get(name);
        if (list == null)
            return new CompiledModule[0];
        CompiledModule[] result = new CompiledModule[list.size()];
        list.toArray(result);
        return result;
    }

    public Binding[] getBindingByDestination(String name) {
        List<Binding> list = bindingsByDestination.get(name);
        if (list == null)
            return new Binding[0];
        Binding[] result = new Binding[list.size()];
        list.toArray(result);
        return result;
    }

    private List<CompiledModule> binaries;
    private List<Binding> mapping;

    private List<String> executors;
    private Map<String, List<CompiledModule>> modulesByExecutor;
    private Map<String, List<Binding>> bindingsByDestination;
}
