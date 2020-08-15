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

import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.graph.Channel;

/**
 * The parsed channel description
 */
public class RawChannel extends Entity implements Comparable<RawChannel> {
    public RawChannel(Fragment fragment, String srcModuleName, String srcVariableName, String dstModuleName, String dstVariableName) {
        this.fragment = fragment;
        this.srcModuleName = srcModuleName;
        this.srcVariableName = srcVariableName;
        this.dstModuleName = dstModuleName;
        this.dstVariableName = dstVariableName;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public Channel toNode() {
        return new Channel(fragment, srcModuleName, srcVariableName, dstModuleName, dstVariableName);
    }

    @Override
    public int compareTo(RawChannel other) {
        int k = srcModuleName.compareTo(other.srcModuleName);
        if (k == 0) {
            k = dstModuleName.compareTo(other.dstModuleName);
            if (k == 0) {
                k = srcVariableName.compareTo(other.srcVariableName);
                if (k == 0) {
                    k = dstVariableName.compareTo(other.dstVariableName);
                }
            }
        }
        return k;
    }

    private Fragment fragment;
    private String srcModuleName;
    private String srcVariableName;
    private String dstModuleName;
    private String dstVariableName;
}
