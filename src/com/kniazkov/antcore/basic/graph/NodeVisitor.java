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

import com.kniazkov.antcore.basic.common.SyntaxError;

/**
 * A visitor for nodes
 */
public abstract class NodeVisitor {
    public void visit(Argument obj) throws SyntaxError {
    }
    public void visit(ArgumentList obj) throws SyntaxError {
    }
    public void visit(Assignment obj) throws SyntaxError {
    }
    public void visit(BinaryOperation obj) throws SyntaxError {
    }
    public void visit(Casting obj) throws SyntaxError {
    }
    public void visit(CodeBlock obj) throws SyntaxError {
    }
    public void visit(Constant obj) throws SyntaxError {
    }
    public void visit(ConstantList obj) throws SyntaxError {
    }
    public void visit(ConstantModifier obj) throws SyntaxError {
    }
    public void visit(DataSet obj) throws SyntaxError {
    }
    public void visit(DataTypeReference obj) throws SyntaxError {
    }
    public void visit(Field obj) throws SyntaxError {
    }
    public void visit(Function obj) throws SyntaxError {
    }
    public void visit(FunctionCall obj) throws SyntaxError {
    }
    public void visit(Module obj) throws SyntaxError {
    }
    public void visit(NativeFunction obj) throws SyntaxError {
    }
    public void visit(Pointer obj) throws SyntaxError {
    }
    public void visit(Program obj)  throws SyntaxError {
    }
    public void visit(StatementExpression obj)  throws SyntaxError {
    }
    public void visit(StatementList obj)  throws SyntaxError {
    }
    public void visit(StringType obj)  throws SyntaxError {
    }
    public void visit(Struct obj)  throws SyntaxError {
    }
    public void visit(VariableReference obj)  throws SyntaxError {
    }
}
