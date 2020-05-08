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

import com.kniazkov.antcore.basic.graph.Program;
import com.kniazkov.antcore.basic.SyntaxError;
import com.kniazkov.antcore.basic.parser.Parser;

public class Main {
    public static void main(String[] args) {
        String source =
                "MODULE SERVER LOCAL ' comment\n" +
                "\n" +
                        "END MODULE\n" +
                "MODULE INTERFACE\n" +
                "END MODULE";
        try {
            Program program = Parser.parse(null, source);
            System.out.println(program.toSourceCode());
        } catch (SyntaxError syntaxError) {
            syntaxError.printStackTrace();
        }
    }
}
