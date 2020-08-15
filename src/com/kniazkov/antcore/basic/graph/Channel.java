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

import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;

/**
 * Connecting two variables of different modules
 */
public class Channel extends Node {
    public Channel(Fragment fragment, String srcModuleName, String srcVariableName, String dstModuleName, String dstVariableName) {
        this.fragment = fragment;
        this.srcModuleName = srcModuleName;
        this.srcVariableName = srcVariableName;
        this.dstModuleName = dstModuleName;
        this.dstVariableName = dstVariableName;
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    protected Node[] getChildren() {
        return new Node[0];
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }

    void setOwner(Transmission owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return owner;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append(srcModuleName).append('.').append(srcVariableName)
                .append(" TO ").append(dstModuleName).append('.').append(dstVariableName).append('\n');
    }

    private Fragment fragment;
    private Transmission owner;
    private String srcModuleName;
    private String srcVariableName;
    private String dstModuleName;
    private String dstVariableName;
}
