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

import com.kniazkov.antcore.basic.bytecodebuilder.CompilationUnit;

/**
 * The node represents a string constant
 */
public class StringNode extends Expression {
    public StringNode(String value) {
        this.value = value;
    }

    @Override
    public DataType getType() {
        if (type == null)
            type = new StringType(value.length());
        return type;
    }

    @Override
    public Object calculate() {
        return value;
    }

    @Override
    void setOwner(ExpressionOwner owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
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

    @Override
    public void load(CompilationUnit cu) {
        assert(false);
    }

    private ExpressionOwner owner;
    private String value;
    private DataType type;
}
