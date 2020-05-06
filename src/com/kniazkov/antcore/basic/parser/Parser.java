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
package com.kniazkov.antcore.basic.parser;

import com.kniazkov.antcore.basic.graph.Module;
import com.kniazkov.antcore.basic.graph.Program;
import com.kniazkov.antcore.basic.parser.exceptions.*;
import com.kniazkov.antcore.basic.parser.tokens.*;

import java.util.*;

/**
 * The parser for basic code
 */
public class Parser {

    /**
     * Parses a source code
     * @param source The source code
     * @return The syntax tree
     */
    public static Program parse(String source) throws ParseError
    {
        // prepare an array of lines:
        String[] list = source.split("\n");
        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            String text = list[i].trim();
            if (text.length() > 0) {
                List<Token> tokens = scan(text);
                if (tokens != null && tokens.size() > 0) {
                    lines.add(new Line(i + 1, text, tokens));
                }
            }
        }

        // parse
        Parser parser = new Parser();
        parser.parse(lines);

        // convert to node
        Map<String, Module> modules = new TreeMap<>();
        for (Map.Entry<String, RawModule> entry : parser.rawModules.entrySet()) {
            Module module = (Module)entry.getValue().toNode();
            modules.put(module.getName(), module);
        }
        Program program = new Program(modules);
        return program;
    }

    /**
     * Scanner, splits string by tokens
     * @param text The string
     * @return The list of tokens
     */
    private static List<Token> scan(String text) throws ParseError {

        // remove comment
        int comment = text.indexOf('\'');
        if (comment >= 0) {
            text = text.substring(0, comment).trim();
            if (text.length() == 0)
                return null;
        }

        ArrayList<Token> tokens = new ArrayList<>();
        Source src = new Source(text);
        while(src.valid()) {
            Token token = getToken(src);
            tokens.add(token);
        }
        return tokens;
    }

    /**
     * Tokenizer
     * @param src The source string
     * @return The next token
     */
    private static Token getToken(Source src) throws ParseError {
        char c = src.get();

        while (isSpace(c))
            c = src.next();
        if (c == 0)
            return null;

        if (isLetter(c)) {
            StringBuilder b = new StringBuilder();
            do {
                b.append(c);
                c = src.next();
            } while(isLetter(c) || isDigit(c));
            String name = b.toString();
            switch (name) {
                case "MODULE":
                    return new KeywordModule();
                case "END":
                    return new KeywordEnd();
            }
            return new Identifier(name);
        }

        return null;
    }

    /**
     * Sequence of chars
     */
    private static class Source {
        public Source(String text) {
            this.text = text;
            this.idx = 0;
            this.maxIdx = text.length();
        }

        /**
         * @return true if has more chars
         */
        public boolean valid() {
            return idx < maxIdx;
        }

        /**
         * @return current char (or 0)
         */
        public char get() {
            return valid() ? text.charAt(idx) : 0;
        }

        /**
         * @return next char (or 0)
         */
        public char next() {
            idx++;
            return get();
        }

        private String text;
        private int idx;
        private int maxIdx;
    }

    private static boolean isLetter(char c) {
        return ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_');
    }

    private static boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    private static boolean isSpace(char c) {
        return (c == ' ' || c == '\t');
    }

    private Map<String, RawModule> rawModules;

    private Parser() {
        rawModules = new TreeMap<>();
    }

    /**
     * Parse a source code
     * @param lines The source code parsed to lines
     * @return The syntax tree
     */
    private void parse(List<Line> lines) throws ParseError {
        parseProgram(lines.iterator());
    }

    /**
     * Parse a whole program and extract all modules
     * @param iterator the iterator by lines
     */
    private void parseProgram(Iterator<Line> iterator) throws ParseError {
        while(iterator.hasNext()) {
            Line line = iterator.next();
            assert(line.tokens.size() > 0);
            if (line.tokens.get(0) instanceof KeywordModule) {
                parseModule(line, iterator);
            }
            else
                throw new UnexpectedSequence(line);
        }
    }

    /**
     * Parse a module
     * @param declaration the line contains module declaration
     * @param iterator the iterator by lines
     */
    private void parseModule(Line declaration, Iterator<Line> iterator) throws ParseError {
        Iterator<Token> tokens = declaration.tokens.iterator();
        tokens.next();

        if (!tokens.hasNext())
            throw new ExpectedModuleName(declaration);
        Token token = tokens.next();
        if (!(token instanceof Identifier))
            throw new ExpectedModuleName(declaration);
        String name = ((Identifier)token).getName();
        if (rawModules.containsKey(name))
            throw new ModuleAlreadyExists(declaration, name);

        String executor = null;
        if (tokens.hasNext()) {
            token = tokens.next();
            if (!(token instanceof Identifier))
                throw new ExpectedModuleExecutor(declaration);
            executor = ((Identifier)token).getName();
        }

        if (tokens.hasNext())
            throw new UnrecognizedSequence(declaration);

        Line line = declaration;
        List<Line> body = new ArrayList<>();
        while(iterator.hasNext()) {
            line = iterator.next();
            if (line.tokens.size() >= 2
                    && line.tokens.get(0) instanceof KeywordEnd && line.tokens.get(1) instanceof KeywordModule) {
                if (line.tokens.size() > 2)
                    throw new UnrecognizedSequence(line);
                RawModule module = new RawModule(name, executor, body);
                rawModules.put(name, module);
                return;
            }
            body.add(line);
        }
        throw new UnexpectedEndOfFile(line);
    }
}
