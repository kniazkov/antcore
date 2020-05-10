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
import com.kniazkov.antcore.basic.Fragment;
import com.kniazkov.antcore.basic.SyntaxError;
import com.kniazkov.antcore.basic.graph.DataType;
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
    public static Program parse(String fileName, String source) throws SyntaxError
    {
        // prepare an array of lines:

        String[] list = source.split("\n");
        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            String code = list[i].trim();
            if (code.length() > 0) {
                Fragment fragment = new Fragment(fileName, i + 1, code);
                List<Token> tokens = scan(fragment);
                if (tokens != null && tokens.size() > 0) {
                    tokens = parseBrackets(fragment, tokens);
                    lines.add(new Line(fragment, tokens));
                }
            }
        }

        // parse

        Parser parser = new Parser();
        parser.parse(lines);

        // convert to node

        Map<String, Module> modules = new TreeMap<>();
        for (Map.Entry<String, RawModule> entry : parser.rawModules.entrySet()) {
            Module module = entry.getValue().toNode();
            modules.put(module.getName(), module);
        }

        Map<String, DataType> dataTypes = new TreeMap<>();
        for (Map.Entry<String, RawDataType> entry : parser.rawDataTypes.entrySet()) {
            DataType dataType = entry.getValue().toNode();
            dataTypes.put(dataType.getName(), dataType);
        }

        return new Program(modules, dataTypes);
    }

    /**
     * Scanner, splits string by tokens
     * @param fragment The fragment of code
     * @return The list of tokens
     */
    private static List<Token> scan(Fragment fragment) throws SyntaxError {
        String text = fragment.getCode();

        // remove comment
        int comment = text.indexOf('\'');
        if (comment >= 0) {
            text = text.substring(0, comment).trim();
            if (text.length() == 0)
                return null;
        }

        ArrayList<Token> tokens = new ArrayList<>();
        Source source = new Source(text);
        while(source.valid()) {
            Token token = getToken(fragment, source);
            tokens.add(token);
        }
        return tokens;
    }

    /**
     * Tokenizer
     * @param src The source string
     * @return The next token
     */
    private static Token getToken(Fragment fragment, Source src) throws SyntaxError {
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
                    return KeywordModule.getInstance();
                case "DATA":
                    return KeywordData.getInstance();
                case "END":
                    return KeywordEnd.getInstance();
                case "PRIVATE":
                    return KeywordPrivate.getInstance();
                case "PROTECTED":
                    return KeywordProtected.getInstance();
                case "PUBLIC":
                    return KeywordPublic.getInstance();
                case "INPUT":
                    return KeywordInput.getInstance();
                case "OUTPUT":
                    return KeywordOutput.getInstance();
                case "AS":
                    return KeywordAs.getInstance();
                case "TO":
                    return KeywordTo.getInstance();
                case "POINTER":
                    return KeywordPointer.getInstance();
                case "TYPE":
                    return KeywordType.getInstance();
                case "FUNCTION":
                    return KeywordFunction.getInstance();
            }
            return new Identifier(name);
        }

        if (c == ',') {
            src.next();
            return Comma.getInstance();
        }

        if (c == '(') {
            src.next();
            return OpenRoundBracket.getInstance();
        }

        if (c == ')') {
            src.next();
            return CloseRoundBracket.getInstance();
        }

        throw new UnknownCharacter(fragment, c);
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

    /**
     * Parsing of all brackets (including nested)
     * @param fragment the fragment of source code
     * @param tokens the list of tokens
     * @return a list of tokens where some tokens combined into groups
     */
    private static List<Token> parseBrackets(Fragment fragment, List<Token> tokens) throws SyntaxError {
        List<Token> result = new ArrayList<>();
        parseBrackets(fragment, tokens.iterator(), result, '\0');
        return Collections.unmodifiableList(result);
    }

    /**
     * Parsing of all brackets (recursive method)
     * @param fragment fragment of source code
     * @param src source iterator
     * @param dst destination list
     * @param closeBracket a bracket that closes the sequence
     */
     private static void parseBrackets(Fragment fragment, Iterator<Token> src, List<Token> dst, char closeBracket) throws SyntaxError {
        while(src.hasNext()) {
            Token token = src.next();
            if (token instanceof Bracket) {
                Bracket bracket = (Bracket) token;
                if (bracket.isClosed()) {
                    if (bracket.getSymbol() != closeBracket)
                        throw new BracketDoesNotMatch(fragment, bracket.getPairSymbol(), closeBracket);
                    return;
                }
                else {
                    List<Token> sublist = new ArrayList<>();
                    parseBrackets(fragment, src, sublist, bracket.getPairSymbol());
                    BracketsPair bracketsPair = BracketsPair.create(bracket.getSymbol(), sublist);
                    dst.add(bracketsPair);
                }
            }
            else {
                dst.add(token);
            }
        }
        if (closeBracket != '\0')
            throw new MissedBracket(fragment, closeBracket);
    }

    private Map<String, RawModule> rawModules;
    private Map<String, RawDataType> rawDataTypes;
    private List<RawFunction> rawFunctions;

    private Parser() {
        rawModules = new TreeMap<>();
        rawDataTypes = new TreeMap<>();
        rawFunctions = new ArrayList<>();
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
            List<Token> tokens = line.getTokens();
            assert(tokens.size() > 0);
            if (tokens.get(0) instanceof KeywordModule) {
                parseModule(line, iterator);
            }
            else if (tokens.get(0) instanceof KeywordType) {
                parseStructure(line, iterator);
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
            List<Token> tokens = line.getTokens();
            assert(tokens.size() > 0);
            if (tokens.get(0) instanceof KeywordData) {
                RawDataSet dataSet = parseDataSet(line, iterator, DataPrefix.PRIVATE);
                module.setData(dataSet);
            }
            else if (tokens.get(0) instanceof KeywordFunction) {
                RawFunction function = parseFunction(line, iterator);
                module.addFunction(function);
                rawFunctions.add(function);
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

    /**
     * Parse a struct
     * @param declaration the line contains struct declaration
     * @param iterator the iterator by lines
     */
    private void parseStructure(Line declaration, Iterator<Line> iterator) throws SyntaxError {
        Iterator<Token> tokens = declaration.getTokens().iterator();
        tokens.next();

        if (!tokens.hasNext())
            throw new ExpectedTypeName(declaration);
        Token tokenStructName = tokens.next();
        if (!(tokenStructName instanceof Identifier))
            throw new ExpectedTypeName(declaration);
        String structName = ((Identifier) tokenStructName).getName();
        if (rawDataTypes.containsKey(structName))
            throw new TypeAlreadyExists(declaration, structName);

        if (tokens.hasNext())
            throw new UnrecognizedSequence(declaration);

        RawStruct struct = new RawStruct(declaration.getFragment(), structName);

        Line line = declaration;
        while(iterator.hasNext()) {
            line = iterator.next();
            List<Token> content = line.getTokens();
            if (content.size() >= 3 && content.get(0) instanceof Identifier) {
                tokens = content.iterator();
                Identifier fieldName = (Identifier)tokens.next();
                Token asKeyword = tokens.next();
                if (!(asKeyword instanceof KeywordAs))
                    throw new ExpectedAsKeyword(line);
                RawDataType type = parseDataType(line, tokens);
                RawField field = new RawField(line.getFragment(), fieldName.getName(), type);
                struct.addField(field);
            }
            else if (content.size() >= 2
                    && content.get(0) instanceof KeywordEnd && content.get(1) instanceof KeywordType) {
                if (content.size() > 2)
                    throw new UnrecognizedSequence(line);
                rawDataTypes.put(structName, struct);
                return;
            }
        }
        throw new UnexpectedEndOfFile(line);
    }

    /**
     * Parse a function
     * @param declaration the line contains function declaration
     * @param iterator the iterator by lines
     */
    private RawFunction parseFunction(Line declaration, Iterator<Line> iterator) throws SyntaxError {
        Iterator<Token> tokens = declaration.getTokens().iterator();
        tokens.next();

        if (!tokens.hasNext())
            throw new ExpectedFunctionName(declaration);
        Token token = tokens.next();
        if (!(token instanceof Identifier))
            throw new ExpectedFunctionName(declaration);
        String name = ((Identifier)token).getName();

        Token nextToken = null;
        List<RawArgument> arguments = null;
        if (tokens.hasNext()) {
            nextToken = tokens.next();
            if (nextToken instanceof RoundBracketsPair) {
                Iterator<Token> argIterator = ((RoundBracketsPair) nextToken).getTokens().iterator();
                boolean oneMoreArg = false;
                arguments = new ArrayList<>();
                while(true) {
                    if (!argIterator.hasNext())
                    {
                        if (!oneMoreArg)
                            break;
                        throw new ExpectedArgument(declaration);
                    }
                    Token argNameToken = argIterator.next();
                    if (!(argNameToken instanceof Identifier))
                        throw new ExpectedArgument(declaration);
                    String argName = ((Identifier) argNameToken).getName();
                    if (!argIterator.hasNext())
                        throw new ExpectedAsKeyword(declaration);
                    Token asKeyword = argIterator.next();
                    if (!(asKeyword instanceof KeywordAs))
                        throw new ExpectedAsKeyword(declaration);
                    RawDataType argType = parseDataType(declaration, argIterator);
                    RawArgument argument = new RawArgument(argName, argType);
                    arguments.add(argument);
                    if (argIterator.hasNext()) {
                        Token comma = argIterator.next();
                        if (!(comma instanceof Comma))
                            throw new ExpectedComma(declaration);
                        oneMoreArg = true;
                    }
                    else
                        break;
                }
                nextToken = tokens.hasNext() ? tokens.next() : null;
            }
        }

        RawDataType returnType = null;
        if (nextToken != null) {
            if (!(nextToken instanceof KeywordAs))
                throw new ExpectedReturnType(declaration);
            returnType = parseDataType(declaration, tokens);
            if (tokens.hasNext())
                throw new UnrecognizedSequence(declaration);
        }

        Line line = declaration;
        List<Line> body = new ArrayList<>();
        while(iterator.hasNext()) {
            line = iterator.next();
            List<Token> content = line.getTokens();
            if (content.size() >= 2
                    && content.get(0) instanceof KeywordEnd && content.get(1) instanceof KeywordFunction) {
                if (content.size() > 2)
                    throw new UnrecognizedSequence(line);
                return new RawFunction(declaration.getFragment(), name, arguments, returnType, body);
            }
            body.add(line);
        }
        throw new UnexpectedEndOfFile(line);
    }
}
