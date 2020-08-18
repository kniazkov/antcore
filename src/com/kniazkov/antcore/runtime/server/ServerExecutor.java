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
package com.kniazkov.antcore.runtime.server;

import com.kniazkov.antcore.basic.bytecode.CompiledModule;
import com.kniazkov.antcore.basic.bytecode.FullAddress;
import com.kniazkov.antcore.runtime.Executor;
import com.kniazkov.antcore.runtime.Runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The 'SERVER' executor
 */
public class ServerExecutor extends Executor {
    public ServerExecutor(Runtime runtime) {
        super(runtime);
    }

    @Override
    public String getName() {
        return "SERVER";
    }

    @Override
    public void setModuleList(CompiledModule[] modules) {
        antList = new ArrayList<>();
        antMap = new TreeMap<>();
        for (CompiledModule module : modules) {
            Ant ant = new Ant(this, module.getBytecode());
            antList.add(ant);
            antMap.put(module.getName(), ant);
        }
    }

    @Override
    protected void init() {
        // do nothing
    }

    @Override
    protected int getFrequency() {
        return 100;
    }

    @Override
    protected boolean tick() {
        if (antList == null)
            return false;

        for (Ant ant : antList) {
            ant.tick();
        }

        return true;
    }

    @Override
    public boolean read(FullAddress address, int size, byte[] buffer) {
        assert (address.getExecutor().equals("SERVER"));
        if (antMap != null) {
            Ant ant = antMap.get(address.getModule());
            if (ant != null) {
                ant.read(address.getOffset(), size, buffer);
                return true;
            }
        }
        return false;
    }

    @Override
    public void write(FullAddress address, int size, byte[] buffer) {
        assert (address.getExecutor().equals("SERVER"));
        if (antMap != null) {
            Ant ant = antMap.get(address.getModule());
            if (ant != null) {
                ant.write(address.getOffset(), size, buffer);
            }
        }
    }

    List<Ant> antList;
    Map<String, Ant> antMap;
}
