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

import com.kniazkov.antcore.basic.bytecode.Binding;
import com.kniazkov.antcore.basic.bytecode.CompiledModule;
import com.kniazkov.antcore.basic.bytecode.ShortAddress;
import com.kniazkov.antcore.runtime.Executor;
import com.kniazkov.antcore.runtime.Runtime;
import com.kniazkov.webserver.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.HashSet;

/**
 * The 'WEB' executor (i.e. web interface)
 */
public class WebExecutor extends Executor {
    public WebExecutor(Runtime runtime) {
        super(runtime);
        this.antsByUId = new TreeMap<>();
        this.antsByModule = new TreeMap<>();
    }

    @Override
    protected boolean tick() {
        long currentTime = getTicks();
        List<String> died = null;
        for (Map.Entry<String, Ant> entry : antsByUId.entrySet()) {
            Ant ant = entry.getValue();
            if (currentTime - ant.timestamp > antsLifetime) {
                String uid = ant.getUId();
                if (died == null)
                    died = new ArrayList<>();
                died.add(uid);
            }
        }

        if (died != null) {
            for (String uid : died) {
                Ant ant = antsByUId.remove(uid);
                antsByModule.get(ant.getModuleName()).remove(ant);
                System.out.println("The ant '" + uid + "' died, population: " + antsByUId.size());
            }
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
            String name = module.getName();
            ModuleInfo info = new ModuleInfo();
            info.module = module;
            this.modules.put(name, info);
            this.antsByModule.put(name, new HashSet<>());
        }
    }

    @Override
    public void setBindingByModule(Binding[] mapping) {
        for (Binding binding : mapping) {
            String module = binding.getDestination().getModule();
            ModuleInfo info = modules.get(module);
            if (info != null) {
                info.mapping.add(binding);
            }
        }
    }

    @Override
    protected void init() {
        webServer = Server.start(new Options(), new Handler(this));
    }

    @Override
    protected int getFrequency() {
        return 10;
    }

    @Override
    public boolean read(ShortAddress address, int size, byte[] buffer) {
        Set<Ant> ants = antsByModule.get(address.getModule());
        if (ants.size() == 1) { // If there more than one copy of the page, we can not read data from them
            Ant ant = ants.iterator().next();
            ant.read(address.getOffset(), size, buffer);
            return true;
        }
        return false;
    }

    ModuleInfo getModuleByName(String name) {
        return modules.get(name);
    }

    private static final long antsLifetime = 10;

    private Map<String, ModuleInfo> modules;
    Server webServer;
    Map<String, Ant> antsByUId;
    Map<String, Set<Ant>> antsByModule;
}
