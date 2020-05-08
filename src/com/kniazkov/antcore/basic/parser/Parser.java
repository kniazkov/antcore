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

import com.kniazkov.antcore.basic.DataPrefix;
import com.kniazkov.antcore.basic.SyntaxError;
import com.kniazkov.antcore.basic.graph.Module;
import com.kniazkov.antcore.basic.graph.Program;
import com.kniazkov.antcore.basic.parser.exceptions.*;
import com.kniazkov.antcore.basic.parser.tokens.*;
import com.kniazkov.antcore.lib.Reference;

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
    public static Program parse(String fileName, String source) throws SyntaxError
    {
        // prepare an array of lines:
        String[] list = source.split("\n");
        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            String code = list[i].trim();
            if (code.length() > 0) {
                List<Token> tokens = scan(code);
                if (tokens != null && tokens.size() > 0) {
                    lines.add(new Line( fileName,i + 1, code, tokens));
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
    private static List<Token> scan(String text) throws SyntaxError {

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
    private static Token getToken(Source src) throws SyntaxError {
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
                case "DATA":
                    return new KeywordData();
                case "END":
                    return new KeywordEnd();
                case "PRIVATE":
                    return new KeywordPrivate();
                case "PROTECTED":
                    return new KeywordProtected();
                case "PUBLIC":
                    return new KeywordPublic();
                case "INPUT":
                    return new KeywordInput();
                case "OUTPUT":
                    return new KeywordOutput();
                case "AS":
                    return new KeywordAs();
                case "TO":
                    return new KeywordTo();
                case "POINTER":
                    return new KeywordPointer();
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
    private void parse(List<Line> lines) throws SyntaxError {
        parseProgram(lines.iterator());
        for (Map.Entry<String, RawModule> entry : rawModules.entrySet()) {
            parseModuleBody(entry.getValue());
        }
    }

    /**
     * Parse a whole program and extract all modules
     * @param iterator the iterator by lines
     */
    private void parseProgram(Iterator<Line> iterator) throws SyntaxError {
        while(iterator.hasNext()) {
            Line line = iterator.next();
            assert(line.getTokens().size() > 0);
            if (line.getTokens().get(0) instanceof KeywordModule) {
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
    private void parseModule(Line declaration, Iterator<Line> iterator) throws SyntaxError {
        Iterator<Token> tokens = declaration.getTokens().iterator();
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
            List<Token> content = line.getTokens();
            if (content.size() >= 2
                    && content.get(0) instanceof KeywordEnd && content.get(1) instanceof KeywordModule) {
                if (content.size() > 2)
                    throw new UnrecognizedSequence(line);
                RawModule module = new RawModule(declaration.getFragment(), name, executor, body);
                rawModules.put(name, module);
                return;
            }
            body.add(line);
        }
        throw new UnexpectedEndOfFile(line);
    }

    /**
     * Parse the body of a module
     * @param module Module
     */
    private void parseModuleBody(RawModule module) throws SyntaxError {
        Iterator<Line> iterator = module.getBody().iterator();
        while (iterator.hasNext()) {
            Line line = iterator.next();
            assert(line.getTokens().size() > 0);
            if (line.getTokens().get(0) instanceof KeywordData) {
                RawDataSet dataSet = parseDataSet(line, iterator, DataPrefix.PRIVATE);
                module.setData(dataSet);
            }
            else
                throw new UnexpectedSequence(line);
        }
    }

    /**
     * Parse a data set
     * @param declaration the line contains data set declaration
     * @param iterator the iterator by lines
     * @param prefixDefault the default data prefix for this scope
     */
    private RawDataSet parseDataSet(Line declaration, Iterator<Line> iterator, DataPrefix prefixDefault) throws SyntaxError {
        Iterator<Token> tokens = declaration.getTokens().iterator();
        tokens.next();

        DataPrefix prefix = DataPrefix.PRIVATE;
        if (tokens.hasNext()) {
            Token tokenPrefix = tokens.next();
            if (tokenPrefix instanceof KeywordPrivate)
                prefix = DataPrefix.PRIVATE;
            else if (tokenPrefix instanceof KeywordProtected)
                prefix = DataPrefix.PROTECTED;
            else if (tokenPrefix instanceof KeywordPublic)
                prefix = DataPrefix.PUBLIC;
            else if (tokenPrefix instanceof KeywordInput)
                prefix = DataPrefix.INPUT;
            else if (tokenPrefix instanceof KeywordOutput)
                prefix = DataPrefix.OUTPUT;
            else
                throw new InvalidDataPrefix(declaration);
        }

        if (tokens.hasNext())
            throw new UnrecognizedSequence(declaration);

        RawDataSet dataSet = new RawDataSet(declaration.getFragment(), prefix, prefixDefault);

        Line line = declaration;
        while(iterator.hasNext()) {
            line = iterator.next();
            List<Token> content = line.getTokens();
            if (content.size() >= 3 && content.get(0) instanceof Identifier) {
                tokens = content.iterator();
                Identifier name = (Identifier)tokens.next();
                Token asKeyword = tokens.next();
                if (!(asKeyword instanceof KeywordAs))
                    throw new ExpectedAsKeyword(line);
                RawDataType type = parseDataType(line, tokens);
                RawField field = new RawField(line.getFragment(), name.getName(), type);
                dataSet.addField(field);
            }
            else if (content.size() >= 2
                    && content.get(0) instanceof KeywordEnd && content.get(1) instanceof KeywordData) {
                if (content.size() > 2)
                    throw new UnrecognizedSequence(line);
                return dataSet;
            }
        }
        throw new UnexpectedEndOfFile(line);
    }

    /**
     * Parse a data type
     * @param line the source code line
     * @param iterator the iterator by tokens
     */
    private RawDataType parseDataType(Line line, Iterator<Token> iterator) throws SyntaxError {
        if (!iterator.hasNext())
            throw new ExpectedDataType(line);

        Token first = iterator.next();

        if (first instanceof Identifier)
            return new RawDataTypeIdentifier(((Identifier) first).getName());

        if (first instanceof KeywordPointer) {
            if (!iterator.hasNext())
                throw new ExpectedToKeyword(line);
            Token toKeyword = iterator.next();
            if (!(toKeyword instanceof KeywordTo))
                throw new ExpectedToKeyword(line);
            RawDataType subType = parseDataType(line, iterator);
            return new RawDataTypePointer(subType);
        }

        throw new ExpectedDataType(line);
    }
}
