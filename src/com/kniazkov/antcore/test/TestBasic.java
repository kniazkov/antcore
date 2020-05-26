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
package com.kniazkov.antcore.test;

import com.kniazkov.antcore.basic.bytecode.CompiledModule;
import com.kniazkov.antcore.basic.bytecode.CompiledProgram;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.graph.Analyzer;
import com.kniazkov.antcore.basic.graph.Program;
import com.kniazkov.antcore.basic.parser.Parser;
import com.kniazkov.antcore.basic.virtualmachine.*;
import com.kniazkov.antcore.lib.ByteBuffer;
import com.kniazkov.antcore.lib.FileIO;

import java.io.File;
import java.util.Map;

/**
 * Test system for antcore basic
 */
public class TestBasic {
    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        Map<String, NativeFunction> stdlib = StandardLibrary.getFunctions();
        class Print implements NativeFunction {
            private StringBuilder output;

            public void reset() {
                output = new StringBuilder();
            }

            public String getData() {
                return output.toString();
            }

            @Override
            public void exec(ByteBuffer memory, int SP) {
                int address = memory.getInt(SP + 4);
                StringData string = StringData.read(memory, address);
                output.append(string);
            }
        }
        Print fakeOutput = new Print();
        stdlib.put("print", fakeOutput);

        File testFolder = new File("tests/basic");
        for (File test : testFolder.listFiles()) {
            if (test.isDirectory()) {
                String name = test.getName();
                String source = FileIO.readFileToString("tests/basic/" + name + "/program.txt");
                if (source != null) {
                    try {
                        Program program = Parser.parse(null, source);
                        Analyzer.analyze(program);
                        CompiledProgram compiledProgram = program.compile();
                        CompiledModule[] modules =  compiledProgram.getModulesByExecutor("SERVER");
                        if (modules == null || modules.length != 1) {
                            failed++;
                            System.out.println(name + ": bad test");
                        }
                        else {
                            fakeOutput.reset();
                            VirtualMachine virtualMachine = new VirtualMachine(modules[0].getBytecode(),
                                    65536,
                                    StandardLibrary.getFunctions());
                            virtualMachine.run();
                            ErrorCode errorCode = virtualMachine.getErrorCode();
                            if (errorCode != ErrorCode.OK) {
                                failed++;
                                System.out.println(name + ": VM failed " + errorCode);
                            }
                            String expectedResult = FileIO.readFileToString("tests/basic/" + name + "/output.txt");
                            String actualResult = fakeOutput.getData();
                            if (expectedResult != null && expectedResult.equals(actualResult)) {
                                passed++;
                                System.out.println(name + ": passed");
                            }
                            else {
                                failed++;
                                System.out.println(name + ": does not match");
                                FileIO.writeStringToFile("tests/basic/" + name + "/actual_output.txt",
                                        actualResult);
                            }
                        }
                    } catch (SyntaxError syntaxError) {
                        failed++;
                        System.out.println(name + ": " + syntaxError.toString());
                    }
                }
                else {
                    failed++;
                    System.out.println(name + ": no code");
                }
            }
        }
        System.out.println("\nDone, total: " + (passed + failed) + ", passed: " + passed + ", failed: " + failed);
    }
}
