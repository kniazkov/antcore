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
import com.kniazkov.antcore.basic.virtualmachine.StandardLibrary;
import com.kniazkov.antcore.basic.virtualmachine.VirtualMachine;
import com.kniazkov.antcore.lib.ByteList;
import com.kniazkov.antcore.runtime.Channel;

import java.util.ArrayList;
import java.util.List;

/**
 * An ant, i.e. minimal execution unit that contains own memory space
 */
public class Ant {
    public Ant(ServerExecutor executor, ByteList code) {
        this.executor = executor;
        vm = new VirtualMachine(code, 1048576, StandardLibrary.getFunctions());
        channels = new ArrayList<>();
    }

    /**
     * Periodically performed task
     */
    void tick() {
        for (Channel channel : channels) {
            channel.transmit();
        }
        vm.run();
    }

    /**
     * Read data by absolute address
     * @param address absolute address
     * @param size size
     * @param buffer destination buffer
     */
    void read(int address, int size, byte[] buffer) {
        vm.read(address, size, buffer);
    }

    /**
     * Bind a source of data
     * @param binding the binding structure
     */
    void bind(Binding binding) {
        Channel channel = new Channel(executor.getRuntime(), vm, binding);
        channels.add(channel);
    }

    private ServerExecutor executor;
    private VirtualMachine vm;
    private List<Channel> channels;
}
