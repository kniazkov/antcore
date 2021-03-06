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
import com.kniazkov.antcore.basic.bytecode.FullAddress;
import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.exceptions.*;

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

    /**
     * Bind names
     */
    void bindNames(Program program) throws SyntaxError {
        srcModule = program.getModuleByName(srcModuleName);
        if (srcModule == null)
            throw new UnknownModule(fragment, srcModuleName);

        srcVariable = srcModule.getOutputFieldByName(srcVariableName);
        if (srcVariable == null)
            throw new UnknownOutputField(fragment, srcModuleName, srcVariableName);

        dstModule = program.getModuleByName(dstModuleName);
        if (dstModule == null)
            throw new UnknownModule(fragment, dstModuleName);

        dstVariable = dstModule.getInputFieldByName(dstVariableName);
        if (dstVariable == null)
            throw new UnknownInputField(fragment, dstModuleName, dstVariableName);
    }

    /*
     * Check types of the source-destination pair
     */
    void checkTypes() throws SyntaxError {
        DataType srcType = srcVariable.getType().getPureType();
        DataType dstType = dstVariable.getType().getPureType();
        if (!srcType.isBinaryAnalog(dstType))
            throw new NonTransferableTypes(fragment, srcType.getName(), dstType.getName());
    }

    /**
     * @return a binding of two data regions (absolute addresses)
     *   that owned by different modules
     */
    public Binding getBinding() throws SyntaxError {
        FullAddress source = new FullAddress(srcModule.getNotNullExecutor(), srcModuleName,
                srcModule.getDynamicDataOffset() + srcVariable.getAbsoluteOffset().get());
        FullAddress destination = new FullAddress(dstModule.getNotNullExecutor(), dstModuleName,
                dstModule.getDynamicDataOffset() + dstVariable.getAbsoluteOffset().get());
        int size = srcVariable.getType().getSize();
        assert(size == dstVariable.getType().getSize());
        return new Binding(source, destination, size);
    }

    private Fragment fragment;
    private Transmission owner;
    private String srcModuleName;
    private Module srcModule;
    private String srcVariableName;
    private Field srcVariable;
    private String dstModuleName;
    private Module dstModule;
    private String dstVariableName;
    private Field dstVariable;
}
