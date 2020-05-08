package com.kniazkov.antcore.basic.parser.tokens;

import com.kniazkov.antcore.basic.graph.Node;
import com.kniazkov.antcore.basic.parser.Token;

public class Identifier extends Token {
    public Identifier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    private String name;
}
