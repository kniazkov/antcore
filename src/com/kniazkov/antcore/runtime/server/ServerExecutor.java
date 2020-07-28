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
import com.kniazkov.antcore.runtime.Executor;

import java.util.ArrayList;
import java.util.List;

/**
 * The 'SERVER' executor
 */
public class ServerExecutor extends Executor {
    @Override
    public String getName() {
        return "SERVER";
    }

    @Override
    public void setModuleList(CompiledModule[] modules) {
        ants = new ArrayList<>();
        for (CompiledModule module : modules) {
            Ant ant = new Ant(this, module.getBytecode());
            ants.add(ant);
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
        if (ants == null)
            return false;

        for (Ant ant : ants) {
            ant.tick();
        }

        return true;
    }

    List<Ant> ants;
}
