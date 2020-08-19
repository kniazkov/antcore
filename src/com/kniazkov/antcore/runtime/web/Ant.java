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
import com.kniazkov.antcore.basic.virtualmachine.VirtualMachine;
import com.kniazkov.antcore.lib.ByteList;
import com.kniazkov.antcore.runtime.Channel;

import java.util.*;

/**
 * An ant, i.e. minimal execution unit that contains own memory space
 */
public class Ant {
    public Ant(long timestamp, String module, WebExecutor executor, ByteList code, List<Binding> mapping) {
        this.timestamp = timestamp;
        this.module = module;
        vm = new VirtualMachine(code, 65536, WebLibrary.create(this));
        uid = UUID.randomUUID().toString();

        channels = new ArrayList<>();
        for (Binding binding : mapping) {
            Channel channel = new Channel(executor.getRuntime(), vm, binding);
            channels.add(channel);
        }

        instructions = new LinkedList<>();

        widgets = new TreeMap<>();
        widgetId = 0;
        widgets.put(0, new Body());
    }

    /**
     * Periodically performed task
     */
    public void tick() {
        for (Channel channel : channels) {
            channel.transmit();
        }
        vm.run();
    }

    public String getUId() {
        return uid;
    }

    /**
     * Read data by absolute address
     * @param address absolute address
     * @param size size
     * @param buffer destination buffer
     */
    public void read(int address, int size, byte[] buffer) {
        vm.read(address, size, buffer);
    }

    public String getModuleName() {
        return module;
    }

    Widget createWidget(String type) {
        int id = ++widgetId;
        Widget widget = null;
        switch (type) {
            case "label":
                widget = new Label(id);
                break;
            case "break":
                widget = new Break(id);
                break;
            case "input":
                widget = new Input(id);
                break;
            case "button":
                widget = new Button(id);
                break;
            default:
                return null;
        }
        widgets.put(id, widget);
        return widget;
    }

    long timestamp;
    int transaction;
    private String module;
    private List<Channel> channels;
    private VirtualMachine vm;
    private String uid;
    List<Instruction> instructions;
    Map<Integer, Widget> widgets;
    private int widgetId;
}
