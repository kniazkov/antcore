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

import com.kniazkov.antcore.basic.Fragment;
import com.kniazkov.antcore.basic.SyntaxError;

/**
 * The assignment, i.e. '=' operation
 */
public class Assignment extends Statement implements ExpressionOwner {
    public Assignment(Fragment fragment, Expression left, Expression right) {
        super(fragment);
        this.left = left;
        left.setOwner(this);
        this.right = right;
        right.setOwner(this);
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        left.dfs(visitor);
        right.dfs(visitor);
        accept(visitor);
    }
    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i);
        left.toUsageSourceCode(buff);
        buff.append(" = ");
        right.toUsageSourceCode(buff);
        buff.append('\n');
    }

    private Expression left;
    private Expression right;
}
