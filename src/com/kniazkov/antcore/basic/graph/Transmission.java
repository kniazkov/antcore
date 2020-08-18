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
package com.kniazkov.antcore.basic.graph;

import com.kniazkov.antcore.basic.bytecode.Binding;
import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data transmission descriptor
 */
public class Transmission extends Node {
    public Transmission(Fragment fragment, List<Channel> channels) {
        this.fragment = fragment;
        this.channels = Collections.unmodifiableList(channels);
        for (Channel channel : channels) {
            channel.setOwner(this);
        }
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        Node[] nodes = new Node[channels.size()];
        channels.toArray(nodes);
        return nodes;
    }

    void setOwner(Program owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return owner;
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append("TRANSMISSION\n");
        String i1 = i + i0;
        for (Channel channel : channels) {
            channel.toSourceCode(buff, i1, i0);
        }
        buff.append(i).append("END TRANSMISSION").append('\n');
    }

    public List<Binding> getMapping() throws SyntaxError {
        List<Binding> result = new ArrayList<>();
        for (Channel channel : channels) {
            result.add(channel.getBinding());
        }
        // TODO: optimize the result list
        return result;
    }

    private Fragment fragment;
    private Program owner;
    private List<Channel> channels;
}
