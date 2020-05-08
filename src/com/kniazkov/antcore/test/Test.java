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

import com.kniazkov.antcore.basic.graph.Program;
import com.kniazkov.antcore.basic.SyntaxError;
import com.kniazkov.antcore.basic.parser.Parser;

/**
 * Unit test system
 */
public class Test {
    public static void main(String[] args) {
        boolean flag = parserTesting();
        System.out.println(flag ? "Ok." : "Fail.");
    }

    private static boolean parserTesting() {
        boolean flag = true;
        final String[] tests = {
                "MODULE SERVER\nEND MODULE\n",
                "MODULE SERVER LOCAL\nEND MODULE\n",
                "MODULE SERVER\n\tDATA\n\tEND DATA\nEND MODULE\n",
                "MODULE SERVER\n\tDATA\n\tEND DATA\n\tDATA INPUT\n\tEND DATA\nEND MODULE\n",
                "MODULE SERVER\n\tDATA\n\t\tvalue AS INTEGER\n\tEND DATA\nEND MODULE\n",
                "MODULE SERVER\n\tDATA\n\t\tx AS REAL\n\t\ty AS REAL\n\tEND DATA\nEND MODULE\n"
        };
        int i;
        for (i = 0; i < tests.length; i++) {
            if (!parserTest(i, tests[i]))
                flag = false;
        }
        return flag;
    }

    private static boolean parserTest(int id, String code) {
        try {
            Program program = Parser.parse(null, code);
            String code2 = program.toSourceCode();
            if (!code2.equals(code)) {
                System.out.println("> Test " + id + " failed, expected:\n" + code + "> Actual:\n" + code2);
                return false;
            }
        }
        catch(SyntaxError error) {
            System.out.println("> Test " + id + " failed, parsing error:\n" + code);
            return false;
        }
        return true;
    }
}
