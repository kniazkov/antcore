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

import com.kniazkov.antcore.basic.bytecode.DataSelector;
import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;
import com.kniazkov.antcore.basic.bytecodebuilder.Load;
import com.kniazkov.antcore.basic.bytecodebuilder.RawInstruction;
import com.kniazkov.antcore.basic.common.Offset;
import com.kniazkov.antcore.basic.common.SyntaxError;

import java.util.HashMap;
import java.util.Map;

/**
 * The node represents a string constant
 */
public class StringNode extends Expression {
    public StringNode(String value) {
        this.value = value;
        address = new HashMap<>();
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public DataType getType() {
        if (type == null)
            type = new ConstantModifier(new StringType(value.length()));
        return type;
    }

    @Override
    public Object calculate() {
        return value;
    }

    @Override
    public void toDeclarationSourceCode(StringBuilder buff, String i) {
        toUsageSourceCode(buff);
    }

    @Override
    public void toUsageSourceCode(StringBuilder buff) {
        boolean flag = true;
        buff.append('"');
        for (int i = 0, length = value.length(); i < length; i++) {
            char c = value.charAt(i);
            if (c < 32) {
                if (flag) {
                    buff.append('"');
                    flag = false;
                }
                buff.append(" + CHR(").append(Integer.valueOf(c)).append(')');
            }
            else {
                if (!flag) {
                    buff.append(" + \"");
                }
                if (c == '"')
                    buff.append('"');
                buff.append(c);
            }
        }
        if (flag)
            buff.append('"');
    }

    /**
     * Calculate absolute string address
     * @param module the module
     */
    void calculateAddress(Module module) {
        if(!address.containsKey(module));
            address.put(module, module.getStaticData().getStringOffset(value));
    }

    @Override
    public Expression getPointer() {
        return new GlobalStaticPointer(this, address);
    }

    @Override
    public void genLoad(CompilationUnit cu) throws SyntaxError {
        Module module = cu.getModule();
        assert (address.containsKey(module));
        RawInstruction load = new Load(DataSelector.GLOBAL,
                type.getSize(), cu.getStaticDataOffset(), address.get(module));
        cu.addInstruction(load);
    }

    private String value;
    private DataType type;
    private Map<Module, Offset> address;
}
