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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * All the runtime data
 */
public class Runtime {
    public Runtime() {
        executorList = new ArrayList<>();
        executorMap = new TreeMap<>();
    }

    public void addExecutor(Executor executor) {
        executorList.add(executor);
        executorMap.put(executor.getName(), executor);
    }

    public void run() {
        for (Executor executor : executorList) {
            executor.run();
        }
    }

    public Executor getExecutorByName(String name) {
        return executorMap.get(name);
    }

    private List<Executor> executorList;
    private Map<String, Executor> executorMap;
}
