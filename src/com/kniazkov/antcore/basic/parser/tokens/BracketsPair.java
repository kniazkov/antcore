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
package com.kniazkov.antcore.basic.parser.tokens;

import com.kniazkov.antcore.basic.parser.Token;

import java.util.Collections;
import java.util.List;

/**
 * Token represents brackets pair
 */
public abstract class BracketsPair extends Token {
    public BracketsPair(List<Token> tokens) {
        this.tokens = Collections.unmodifiableList(tokens);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getOpenBracket());
        for (Token token : tokens) {
            builder.append(' ').append(token);
        }
        builder.append(' ').append(getCloseBracket());
        return builder.toString();
    }

    public abstract char getOpenBracket();
    public abstract char getCloseBracket();

    public List<Token> getTokens() {
        return tokens;
    }

    private List<Token> tokens;

    public static BracketsPair create(char openBracket, List<Token> tokens) {
        switch (openBracket) {
            case '(':
                return new RoundBracketsPair(tokens);
        }
        return null;
    }
}
