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

import com.kniazkov.antcore.basic.bytecode.CompiledProgram;
import com.kniazkov.antcore.runtime.server.ServerExecutor;
import com.kniazkov.antcore.runtime.web.WebExecutor;

/**
 * Creates executors and starts bytecode execution
 */
public class Launcher {
    /**
     * Launch a compiled program
     * @param program the program
     */
    public static void launch(CompiledProgram program) {
        Runtime runtime = new Runtime();
        for (String executorName : program.getExecutors()) {
            Executor executor = createExecutor(executorName, runtime);
            if (executor == null) {
                //TODO: throw an exception
                System.err.println("Can't create executor: '" + executorName + '\'');
                return;
            }
            executor.setModuleList(program.getModulesByExecutor(executorName));
            executor.setBindingByModule(program.getBindingByDestination(executorName));
            runtime.addExecutor(executor);
        }

        runtime.run();
    }

    private static Executor createExecutor(String name, Runtime runtime) {
        switch (name) {
            case "WEB":
                return new WebExecutor(runtime);
            case "SERVER":
                return new ServerExecutor(runtime);
        }
        return null;
    }
}
