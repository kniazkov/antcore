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

package com.kniazkov.antcore;

import com.kniazkov.antcore.basic.bytecode.CompiledModule;
import com.kniazkov.antcore.basic.bytecode.CompiledProgram;
import com.kniazkov.antcore.basic.bytecode.Disassembler;
import com.kniazkov.antcore.basic.graph.Analyzer;
import com.kniazkov.antcore.basic.graph.Program;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.parser.Parser;
import com.kniazkov.antcore.basic.virtualmachine.*;
import com.kniazkov.antcore.lib.FileIO;

import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        String source = FileIO.readFileToString("program.txt");
        if (source != null) {
            try {
                Program program = Parser.parse(null, source);
                Analyzer.analyze(program);
                System.out.println(program.toSourceCode());
                CompiledProgram compiledProgram = program.compile();
                for (String executor : compiledProgram.getExecutors()) {
                    for (CompiledModule module : compiledProgram.getModulesByExecutor(executor)) {
                        System.out.println(Disassembler.convert(module.getBytecode()));

                        VirtualMachine virtualMachine = new VirtualMachine(module.getBytecode(), 1024,
                                StandardLibrary.getFunctions());
                        virtualMachine.run();
                        ErrorCode errorCode = virtualMachine.getErrorCode();
                        if (errorCode != ErrorCode.OK) {
                            System.err.println("Virtual machine finished with code " + errorCode +
                                    ", IP = " + virtualMachine.getInstructionPointer());
                        }
                    }
                }
            } catch (SyntaxError syntaxError) {
                syntaxError.printStackTrace();
            }
        }
    }
}
