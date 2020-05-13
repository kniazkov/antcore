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

import com.kniazkov.antcore.basic.SyntaxError;

/**
 * The syntax tree analyzer
 */
public class Analyzer {
    /**
     * The full analysis of the syntax tree
     * @param root the root node
     */
    public static void analyze(Program root) throws SyntaxError {
        bindTypes(root);
        bindNames(root);
        defineTypes(root);
        calculateOffsets(root);
    }

    /**
     * Bind data types by name
     * @param root the root node
     */
    protected static void bindTypes(Program root) throws SyntaxError {
        class Binder extends NodeVisitor {
            @Override
            public void visit(DataTypeReference obj) throws SyntaxError {
                obj.bindType();
            }
        }

        root.dfs(new Binder());
    }

    /**
     * Define data types for operations
     * @param root the root node
     */
    protected static void defineTypes(Program root) throws SyntaxError {
        class Calculator extends NodeVisitor {
            @Override
            public void visit(BinaryOperation obj) throws SyntaxError {
                obj.defineType();
            }
        }

        root.dfs(new Calculator());
    }

    /**
     * Bind names of variables
     * @param root the root node
     */
    protected static void bindNames(Program root) throws SyntaxError {
        class Binder extends NodeVisitor {
            @Override
            public void visit(VariableReference obj) throws SyntaxError {
                obj.bindName();
            }
        }

        root.dfs(new Binder());
    }

    /**
     * Calculate offsets of all fields of all data types, all arguments and
     * local variables
     * @param root the root node
     */
    protected static void calculateOffsets(Program root) throws SyntaxError {
        class Calculator extends NodeVisitor {
            @Override
            public void visit(DataSet obj) throws SyntaxError {
                obj.calculateOffsets();
            }
            @Override
            public void visit(ArgumentList obj) throws SyntaxError {
                obj.calculateOffsets();
            }
        }

        root.dfs(new Calculator());
    }
}
