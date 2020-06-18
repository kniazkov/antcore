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

import java.util.List;

/**
 * The syntax tree analyzer
 */
public class Analyzer {
    /**
     * The full analysis of the syntax tree
     * @param root the root node
     */
    public static void analyze(Program root) throws SyntaxError {
        root.mergeModulesAndCodeBlocks();
        bindTypes(root);
        bindNames(root);
        buildStaticData(root);
        checkTypes(root);
        calculateOffsets(root);
    }

    /**
     * Visit all nodes in the syntax tree
     * @param root the root node
     * @param visitor the visitor
     */
    protected static void visitAll(Node root, NodeVisitor visitor) throws SyntaxError {
        List<Node> list = root.enumerate();
        for (Node node : list) {
            node.accept(visitor);
        }
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

        visitAll(root, new Binder());
    }

    /**
     * Bind names of variables
     * @param root the root node
     */
    protected static void bindNames(Program root) throws SyntaxError {
        class Binder extends NodeVisitor {
            @Override
            public void visit(FunctionCall obj) throws SyntaxError {
                obj.bindName();
            }
            @Override
            public void visit(VariableReference obj) throws SyntaxError {
                obj.bindName();
            }
        }

        visitAll(root, new Binder());
    }

    /**
     * Calculate offsets of all static blocks such as strings
     * @param root the root node
     */
    protected static void buildStaticData(Program root) throws SyntaxError {
        class Builder extends  NodeVisitor {
            Module module;

            Builder(Module module) {
                this.module = module;
            }

            @Override
            public void visit(StringNode obj) throws SyntaxError {
                obj.calculateAddress(module);
            }
        }

        for (Module module : root.getModuleList()) {
            visitAll(module, new Builder(module));
        }
    }

    /**
     * Check, cast and infer data types
     * @param root the root node
     */
    protected static void checkTypes(Program root) throws SyntaxError {
        class Checker extends NodeVisitor {
            @Override
            public void visit(ArgumentList obj) throws SyntaxError {
                obj.checkTypes();
            }
            @Override
            public void visit(Assignment obj) throws SyntaxError {
                obj.checkType();
            }
            @Override
            public void visit(Constant obj) throws SyntaxError {
                obj.checkType();
            }
            @Override
            public void visit(BinaryOperation obj) throws SyntaxError {
                obj.defineType();
            }
            @Override
            public void visit(DataSet obj) throws SyntaxError {
                obj.checkTypes();
            }
            @Override
            public void visit(ElseIf obj) throws SyntaxError {
                obj.checkType();
            }
            @Override
            public void visit(Function obj) throws SyntaxError {
                obj.checkReturnType();
            }
            @Override
            public void visit(FunctionCall obj) throws SyntaxError {
                obj.checkArguments();
            }
            @Override
            public void visit(If obj) throws SyntaxError {
                obj.checkType();
            }
            @Override
            public void visit(NativeFunction obj) throws SyntaxError {
                obj.checkTypes();
            }
            @Override
            public void visit(Variable obj) throws SyntaxError {
                obj.checkType();
            }
        }

        visitAll(root, new Checker());
    }


    /**
     * Calculate offsets of all fields of all data types, all arguments and
     * local variables
     * @param root the root node
     */
    protected static void calculateOffsets(Program root) throws SyntaxError {
        class Calculator extends NodeVisitor {
            @Override
            public void visit(ArgumentList obj) throws SyntaxError {
                obj.calculateSizeAndOffsets();
            }
            @Override
            public void visit(DataSet obj) throws SyntaxError {
                obj.calculateOffsets();
            }
            @Override
            public void visit(Function obj) throws SyntaxError {
                obj.collectVariablesAndCalculateOffsets();
            }
            @Override
            public void visit(Module obj) throws SyntaxError {
                obj.calculateOffsets();
            }
        }

        visitAll(root, new Calculator());
    }

}
