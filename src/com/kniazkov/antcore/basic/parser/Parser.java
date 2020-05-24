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

import com.kniazkov.antcore.basic.common.DataPrefix;
import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.graph.*;
import com.kniazkov.antcore.basic.parser.exceptions.*;
import com.kniazkov.antcore.basic.parser.tokens.*;
import com.kniazkov.antcore.lib.CharIterator;
import com.kniazkov.antcore.lib.FixedPoint;
import com.kniazkov.antcore.lib.Math2;
import com.kniazkov.antcore.lib.RollbackIterator;

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

        List<CodeBlock> codeBlocks = new ArrayList<>();
        for (RawCodeBlock rawCodeBlock : parser.rawCodeBlocks) {
            CodeBlock codeBlock = rawCodeBlock.toNode();
            codeBlocks.add(codeBlock);
        }

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

        return new Program(parser.globalConstantList != null ? parser.globalConstantList.toNode() : null,
            codeBlocks, modules, dataTypes);
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
            StringBuilder buff = new StringBuilder();
            do {
                buff.append(c);
                c = src.next();
            } while(isLetter(c) || isDigit(c));
            String name = buff.toString();
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
                case "OF":
                    return KeywordOf.getInstance();
                case "POINTER":
                    return KeywordPointer.getInstance();
                case "TYPE":
                    return KeywordType.getInstance();
                case "FUNCTION":
                    return KeywordFunction.getInstance();
                case "CONST":
                case "CONSTANT":
                    return KeywordConst.getInstance();
                case "CODE":
                    return KeywordCode.getInstance();
                case "DECLARE":
                    return KeywordDeclare.getInstance();
            }
            return new Identifier(name);
        }

        if (isDigit(c)) {
            StringBuilder tokStr = new StringBuilder();
            try {
                StringBuilder buff = new StringBuilder();
                String number;
                do {
                    buff.append(c);
                    tokStr.append(c);
                    c = src.next();
                } while (isDigit(c));
                number = buff.toString();
                int intValue = Integer.parseInt(number);
                if (c != '.')
                    return new TokenExpression(new IntegerNode(intValue));
                tokStr.append(c);
                c = src.next();
                if (isDigit(c)) {
                    buff = new StringBuilder();
                    int digits = 0;
                    do {
                        digits++;
                        buff.append(c);
                        tokStr.append(c);
                        c = src.next();
                    } while (isDigit(c));
                    number = buff.toString();
                    int fractValue = Integer.parseInt(number);
                    FixedPoint realNumber = new FixedPoint();
                    FixedPoint fractPart = new FixedPoint();
                    FixedPoint divisor = new FixedPoint();
                    realNumber.setInteger(intValue);
                    fractPart.setInteger(fractValue);
                    divisor.setInteger(Math2.powerOf10(digits));
                    FixedPoint.div(fractPart, fractPart, divisor);
                    FixedPoint.add(realNumber, realNumber, fractPart);
                    return new TokenExpression(new RealNode(realNumber));
                }
            } catch (Exception e) {
            }
            throw new WrongNumberFormat(fragment, tokStr.toString());
        }

        if (c == '"') {
            StringBuilder buff = new StringBuilder();
            boolean flag = false;
            do {
                c = src.next();
                while (c != '"') {
                    buff.append(c);
                    c = src.next();
                    if (!src.valid())
                        throw new MissedClosingQuote(fragment);
                }
                c = src.next();
                if (c != '"')
                    flag = false;
                else {
                    buff.append('"');
                    flag = true;
                }
            } while(flag);
            return new TokenExpression(new StringNode(buff.toString()));
        }

        if (isOperator(c)) {
            StringBuilder buff = new StringBuilder();
            do {
                buff.append(c);
                c = src.next();
            } while(isOperator(c));
            String operator = buff.toString();
            switch (operator) {
                case "+":
                    return OperatorPlus.getInstance();
                case "=":
                    return OperatorAssignEquals.getInstance();
            }
            throw new UnknownOperator(fragment, operator);
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
    private static class Source implements CharIterator {
        public Source(String text) {
            this.text = text;
            this.idx = 0;
            this.maxIdx = text.length();
        }

        @Override
        public boolean valid() {
            return idx < maxIdx;
        }

        @Override
        public char get() {
            return valid() ? text.charAt(idx) : 0;
        }

        @Override
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

    private static boolean isOperator(char c) {
        switch(c) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '<':
            case '>':
            case '=':
                return true;
        }
        return false;
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

    private RawConstantList globalConstantList;
    private Map<String, RawModule> rawModules;
    private List<RawCodeBlock> rawCodeBlocks;
    private Map<String, RawDataType> rawDataTypes;
    private List<RawFunction> rawFunctions;

    private Parser() {
        rawModules = new TreeMap<>();
        rawCodeBlocks = new ArrayList<>();
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
        for (RawCodeBlock block : rawCodeBlocks) {
            parseCodeBlockBody(block);
        }
        for (Map.Entry<String, RawModule> entry : rawModules.entrySet()) {
            parseModuleBody(entry.getValue());
        }
        for (RawFunction function : rawFunctions) {
            parseFunctionBody(function);
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
            Token firstToken = tokens.get(0);
            if (firstToken instanceof KeywordConst) {
                if (globalConstantList == null)
                    globalConstantList = new RawConstantList();
                RawConstant constant = parseConstant(line);
                globalConstantList.addConstant(constant);
            }
            else if (firstToken instanceof KeywordType) {
                parseStructure(line, iterator);
            }
            else if (firstToken instanceof KeywordCode) {
                parseCodeBlock(line, iterator);
            }
            else if (firstToken instanceof KeywordModule) {
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
     * Parse a code block
     * @param declaration the line contains module declaration
     * @param iterator the iterator by lines
     */
    private void parseCodeBlock(Line declaration, Iterator<Line> iterator) throws SyntaxError {
        Iterator<Token> tokens = declaration.getTokens().iterator();
        tokens.next();

        List<String> executors = null;
        if (tokens.hasNext()) {
            executors = new ArrayList<>();
            while(tokens.hasNext()) {
                Token tokExecutor = tokens.next();
                if (!(tokExecutor instanceof Identifier))
                    throw new ExpectedExecutor(declaration);
                executors.add(((Identifier) tokExecutor).getName());
                if (tokens.hasNext()) {
                    Token comma = tokens.next();
                    if (!(comma instanceof Comma))
                        throw new ExpectedComma(declaration);
                    if (!tokens.hasNext())
                        throw new ExpectedExecutor(declaration);
                }
            }
        }

        Line line = declaration;
        List<Line> body = new ArrayList<>();
        while(iterator.hasNext()) {
            line = iterator.next();
            List<Token> content = line.getTokens();
            if (content.size() >= 2
                    && content.get(0) instanceof KeywordEnd && content.get(1) instanceof KeywordCode) {
                if (content.size() > 2)
                    throw new UnrecognizedSequence(line);
                RawCodeBlock codeBlock = new RawCodeBlock(declaration.getFragment(), executors, body);
                rawCodeBlocks.add(codeBlock);
                return;
            }
            body.add(line);
        }
        throw new UnexpectedEndOfFile(line);
    }

    /**
     * Parse the body of a code block
     * @param block Module
     */
    private void parseCodeBlockBody(RawCodeBlock block) throws SyntaxError {
        Iterator<Line> iterator = block.getBody().iterator();
        while (iterator.hasNext()) {
            Line line = iterator.next();
            List<Token> tokens = line.getTokens();
            int size  = tokens.size();
            assert(size > 0);
            if (size > 2 && tokens.get(0) instanceof KeywordDeclare &&
                    tokens.get(1) instanceof KeywordFunction) {
                RawNativeFunction nativeFunction = parseNativeFunction(line);
                block.addNativeFunction(nativeFunction);
            }
            /*
            else if (tokens.get(0) instanceof KeywordFunction) {
                RawFunction function = parseFunction(line, iterator);
                block.addFunction(function);
                rawFunctions.add(function);
            }
            */
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
                RollbackIterator<Token> lineIterator = new RollbackIterator<>(content.iterator());
                Identifier name = (Identifier)lineIterator.next();
                Token asKeyword = lineIterator.next();
                if (!(asKeyword instanceof KeywordAs))
                    throw new ExpectedAsKeyword(line);
                RawDataType type = parseDataType(line, lineIterator);
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
     * @param line the line of source code
     * @param iterator the iterator by tokens
     */
    private RawDataType parseDataType(Line line, RollbackIterator<Token> iterator) throws SyntaxError {
        if (!iterator.hasNext())
            throw new ExpectedDataType(line);

        Token first = iterator.next();

        if (first instanceof Identifier) {
            String name = ((Identifier) first).getName();
            if (name.equals("STRING")) {
                if (!iterator.hasNext())
                    return new RawDataTypeString();
                Token next = iterator.next();
                if (!(next instanceof KeywordOf)) {
                    if (next instanceof Comma)
                        return new RawDataTypeString();
                    throw new ExpectedOfKeyword(line);
                }
                TokenExpression length = parseExpression(line, iterator, false);
                return new RawDataTypeString(length);
            }
            return new RawDataTypeIdentifier(name);
        }

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
                RollbackIterator<Token> lineIterator = new RollbackIterator<>(content.iterator());
                Identifier fieldName = (Identifier)lineIterator.next();
                Token asKeyword = lineIterator.next();
                if (!(asKeyword instanceof KeywordAs))
                    throw new ExpectedAsKeyword(line);
                RawDataType type = parseDataType(line, lineIterator);
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
     * Parse line contains a constant declaration
     * @param declaration the line of source code
     */
    private RawConstant parseConstant(Line declaration) throws SyntaxError {
        RollbackIterator<Token> tokens = new RollbackIterator<>(declaration.getTokens().iterator());
        Token token = tokens.next();
        assert(token instanceof KeywordConst);

        if (!tokens.hasNext())
            throw new ExpectedConstantName(declaration);
        token = tokens.next();
        if (!(token instanceof Identifier))
            throw new ExpectedConstantName(declaration);
        String name = ((Identifier)token).getName();

        if (!tokens.hasNext())
            throw new ExpectedAssignmentOperator(declaration);
        token = tokens.next();
        if (!(token instanceof OperatorAssignEquals))
            throw new ExpectedAssignmentOperator(declaration);

        TokenExpression value = parseExpression(declaration, tokens, false);

        RawDataType type = null;
        if (tokens.hasNext()) {
            token = tokens.next();
            if (!(token instanceof KeywordAs))
                throw new ExpectedAsKeyword(declaration);

            type = parseDataType(declaration, tokens);

            if (tokens.hasNext())
                throw new UnexpectedSequence(declaration);
        }

        return new RawConstant(declaration.getFragment(), name, value, type);
    }

    /**
     * Parse a native function
     * @param declaration the line contains native function declaration
     */
    private RawNativeFunction parseNativeFunction(Line declaration) throws SyntaxError {
        RollbackIterator<Token> tokens = new RollbackIterator<>(declaration.getTokens().iterator());
        Token token = tokens.next();
        assert(token instanceof KeywordDeclare);
        token = tokens.next();
        assert(token instanceof KeywordFunction);

        if (!tokens.hasNext())
            throw new ExpectedFunctionName(declaration);
        token = tokens.next();
        if (!(token instanceof Identifier))
            throw new ExpectedFunctionName(declaration);
        String name = ((Identifier)token).getName();

        Token nextToken = null;
        List<RawDataType> arguments = null;
        if (tokens.hasNext()) {
            nextToken = tokens.next();
            if (nextToken instanceof RoundBracketsPair) {
                RollbackIterator<Token> argIterator =
                        new RollbackIterator<>(((RoundBracketsPair) nextToken).getTokens().iterator());
                boolean oneMoreArg = false;
                arguments = new ArrayList<>();
                while(true) {
                    if (!argIterator.hasNext())
                    {
                        if (!oneMoreArg)
                            break;
                        throw new ExpectedArgument(declaration);
                    }
                    RawDataType type = parseDataType(declaration, argIterator);
                    arguments.add(type);
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

        return new RawNativeFunction(declaration.getFragment(), name, arguments, returnType);
    }

    /**
     * Parse a function
     * @param declaration the line contains function declaration
     * @param iterator the iterator by lines
     */
    private RawFunction parseFunction(Line declaration, Iterator<Line> iterator) throws SyntaxError {
        RollbackIterator<Token> tokens = new RollbackIterator<>(declaration.getTokens().iterator());
        Token token = tokens.next();
        assert(token instanceof KeywordFunction);

        if (!tokens.hasNext())
            throw new ExpectedFunctionName(declaration);
        token = tokens.next();
        if (!(token instanceof Identifier))
            throw new ExpectedFunctionName(declaration);
        String name = ((Identifier)token).getName();

        Token nextToken = null;
        List<RawArgument> arguments = null;
        if (tokens.hasNext()) {
            nextToken = tokens.next();
            if (nextToken instanceof RoundBracketsPair) {
                RollbackIterator<Token> argIterator =
                        new RollbackIterator<Token>(((RoundBracketsPair) nextToken).getTokens().iterator());
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

    /**
     * Parse an expression
     * @param line the line of source code
     * @param iterator the iterator by tokens
     * @param ignoreAssignment do not parse assignment operator
     */
    private TokenExpression parseExpression(Line line, RollbackIterator<Token> iterator,
                                            boolean ignoreAssignment) throws SyntaxError {
        if (!iterator.hasNext())
            throw new ExpectedAnExpression(line);

        LinkedList<Token> sequence = new LinkedList<>();
        while(iterator.hasNext()) {
            Token token = iterator.next();
            if (token instanceof OperatorAssignEquals) {
                if (ignoreAssignment) {
                    iterator.rollback(token);
                    break;
                }
                sequence.add(token);
            }
            else if (token instanceof Identifier || token instanceof TokenExpression || token instanceof Operator
                    || token instanceof BracketsPair) {
                sequence.add(token);
            }
            else {
                iterator.rollback(token);
                break;
            }
        }

        if (sequence.isEmpty())
            throw new ExpectedAnExpression(line);

        sequence = parseIdentifiers(line, sequence);
        sequence = parseBinaryOperators(line, sequence, new Class<?>[]{OperatorPlus.class});

        if (sequence.size() != 1)
            throw new UnrecognizedSequence(line);
        Token result = sequence.get(0);
        if (!(result instanceof TokenExpression))
            throw new ExpectedAnExpression(line);
        return (TokenExpression) result;
    }

    /**
     * Parse variables, accessing fields, accessing array elements, calling functions and methods 
     * @param line the line of source code
     * @param rightSequence the sequence of tokens that possibly contains a variable, field access, etc.
     * @return a new token sequence in which identifiers are converted to expressions
     */
    private LinkedList<Token> parseIdentifiers(Line line, LinkedList<Token> rightSequence) throws SyntaxError {
        LinkedList<Token> leftSequence = new LinkedList<>();
        while(!rightSequence.isEmpty()) {
            Token token = rightSequence.removeFirst();
            if (token instanceof Identifier) {
                String name = ((Identifier) token).getName();
                leftSequence.addLast(new TokenExpression(new VariableReference(name, null)));
            }
            else if (token instanceof RoundBracketsPair) {
                if (leftSequence.isEmpty() || leftSequence.peekLast() instanceof Operator) {
                    // expression in brackets
                    TokenExpression expression = parseParenthesizedExpression(line, (RoundBracketsPair) token);
                    leftSequence.addLast(new TokenExpression(new ParenthesizedExpression(expression.toNode())));
                }
                else
                    throw new UnrecognizedSequence(line);
            }
            else
                leftSequence.addLast(token);
        }
        return leftSequence;
    }

    /**
     * Parse binary operators
     * @param line the line of source code
     * @param rightSequence the sequence of tokens that possibly contains an operator
     * @param operators the list of operators
     * @return a new token sequence in which operators are converted to expressions
     */
    private LinkedList<Token> parseBinaryOperators(Line line, LinkedList<Token> rightSequence, Class<?>[] operators) throws SyntaxError {
        if (rightSequence.size() < 3)
            return rightSequence;
        LinkedList<Token> leftSequence = new LinkedList<>();
        while(!rightSequence.isEmpty()) {
            Token token = rightSequence.removeFirst();
            boolean flag = false;
            for (Class<?> operator : operators) {
                if (operator.isInstance(token)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                leftSequence.addLast(token);
            }
            else {
                Operator operator = (Operator)token;
                Token leftOperand = leftSequence.removeLast();
                if (!(leftOperand instanceof TokenExpression))
                    throw new ExpectedLeftExpOper(line, operator.toString());
                Token rightOperand = rightSequence.removeFirst();
                if (!(rightOperand instanceof TokenExpression))
                    throw new ExpectedLeftExpOper(line, operator.toString());

                BinaryOperation binaryOperation = operator.createBinaryOperation(((TokenExpression) leftOperand).toNode(),
                        ((TokenExpression) rightOperand).toNode());
                leftSequence.addLast(new TokenExpression(binaryOperation));
            }
        }
        return leftSequence;
    }

    /**
     * Parse expression in brackets
     * @param line the line of source code
     * @param bracketsPair the pair of brackets that contains an expression
     * @return an expression
     */
    private TokenExpression parseParenthesizedExpression(Line line, RoundBracketsPair bracketsPair) throws SyntaxError {
        RollbackIterator<Token> iterator = new RollbackIterator<>(bracketsPair.getTokens().iterator());
        TokenExpression result = parseExpression(line, iterator, false);
        if (iterator.hasNext())
            throw new UnrecognizedSequence(line);
        return result;
    }

    /**
     * Parse a function body
     * @param function the function
     */
    private void parseFunctionBody(RawFunction function) throws SyntaxError {
        List<RawStatement> statements = new ArrayList<>();
        parseStatementList(function.getRawBody().iterator(), statements);
        function.setRawStatementList(statements);
    }

    /**
     * Parse a list of statements (function/cycle/condition body)
     * @param iterator the iterator by lines
     * @param result the destination list
     */
    private void parseStatementList(Iterator<Line> iterator, List<RawStatement> result) throws SyntaxError {
        while(iterator.hasNext()) {
            Line line = iterator.next();
            List<Token> tokens = line.getTokens();
            assert (tokens.size() > 0);
            Token firstToken = tokens.get(0);
            if (firstToken instanceof Identifier) {
                RawStatement statement = parseStatementExpression(line);
                result.add(statement);
            }
            else
                throw new UnrecognizedSequence(line);
        }
    }

    /**
     * Parse statement expression, i.e. statement contains only one expression
     * @param line the line of source code
     * @return a parsed statement
     */
    private RawStatement parseStatementExpression(Line line) throws SyntaxError {
        RollbackIterator<Token> tokens = new RollbackIterator<>(line.getTokens().iterator());

        TokenExpression left = parseExpression(line, tokens, true);
        if (!tokens.hasNext()) {
            return new RawStatementExpression(line.getFragment(), left);
        }
        Token token = tokens.next();
        if (!(token instanceof OperatorAssignEquals))
            throw new ExpectedAssignmentOperator(line);

        TokenExpression right = parseExpression(line, tokens, false);
        if (tokens.hasNext())
            throw new UnexpectedSequence(line);

        return new RawAssignment(line.getFragment(), left, right);
    }
}
