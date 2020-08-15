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
package com.kniazkov.antcore.basic.parser;

import com.kniazkov.antcore.basic.common.DataPrefix;
import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.graph.Channel;
import com.kniazkov.antcore.basic.graph.DataSet;
import com.kniazkov.antcore.basic.graph.Field;
import com.kniazkov.antcore.basic.graph.Transmission;
import com.kniazkov.antcore.basic.parser.exceptions.DuplicateChannel;

import java.util.*;

/**
 * The parsed list of channels
 */
public class RawTransmission extends Entity {
    RawTransmission(Fragment fragment) {
        this.fragment = fragment;
        this.rawChannels = new TreeSet<>();
    }

    public void addChannel(RawChannel channel) throws SyntaxError {
        if (rawChannels.contains(channel))
            throw new DuplicateChannel(channel.getFragment());
        rawChannels.add(channel);
    }

    public Fragment getFragment() {
        return fragment;
    }

    public Transmission toNode() {
        List<Channel> channels = new ArrayList<>();
        for (RawChannel rawChannel : rawChannels) {
            channels.add(rawChannel.toNode());
        }
        return new Transmission(fragment, channels);
    }

    private Fragment fragment;
    private Set<RawChannel> rawChannels;
}
