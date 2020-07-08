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
package com.kniazkov.antcore.runtime.web;

import com.kniazkov.antcore.basic.bytecode.CompiledModule;
import com.kniazkov.antcore.runtime.Executor;
import com.kniazkov.webserver.*;

import java.util.Map;
import java.util.TreeMap;

/**
 * The 'WEB' executor (i.e. web interface)
 */
public class Web extends Executor {
    public Web() {
        ants = new TreeMap<>();
    }

    @Override
    protected boolean tick() {
        for (Map.Entry<String, Ant> entry : ants.entrySet()) {
            Ant ant = entry.getValue();
            ant.tick();
        }
        return true;
    }

    @Override
    public String getName() {
        return "WEB";
    }

    @Override
    public void setModuleList(CompiledModule[] modules) {
        this.modules = new TreeMap<>();
        for (CompiledModule module : modules) {
            this.modules.put(module.getName(), module);
        }
    }

    @Override
    protected void init() {
        webServer = Server.start(new Options(), new Handler(this));
    }

    CompiledModule getModuleByName(String name) {
        return modules.get(name);
    }

    private Map<String, CompiledModule> modules;
    Server webServer;
    Map<String, Ant> ants;
}
