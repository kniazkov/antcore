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

import com.kniazkov.antcore.basic.bytecode.Binding;
import com.kniazkov.antcore.basic.bytecode.CompiledModule;
import com.kniazkov.antcore.basic.bytecode.ShortAddress;
import com.kniazkov.antcore.runtime.Executor;
import com.kniazkov.antcore.runtime.Runtime;

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
        antList = new Ant[modules.length];
        antMap = new TreeMap<>();
        for (int k = 0; k < modules.length; k++) {
            CompiledModule module = modules[k];
            Ant ant = new Ant(this, module.getBytecode());
            antList[k] = ant;
            antMap.put(module.getName(), ant);
        }
    }

    @Override
    public void setBindingByModule(Binding[] bindingByModule) {
        if (bindingByModule.length == 0)
            return;

        for(Binding binding : bindingByModule) {
            assert(binding.getDestination().getExecutor().equals("SERVER"));
            String module = binding.getDestination().getModule();
            Ant ant = antMap.get(module);
            if (ant != null) {
                ant.bind(binding);
            }
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
    public boolean read(ShortAddress address, int size, byte[] buffer) {
        if (antMap != null) {
            Ant ant = antMap.get(address.getModule());
            if (ant != null) {
                ant.read(address.getOffset(), size, buffer);
                return true;
            }
        }
        return false;
    }

    Ant[] antList;
    Map<String, Ant> antMap;
}
