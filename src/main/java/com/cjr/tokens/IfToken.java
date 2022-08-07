package com.cjr.tokens;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

// For while / if / catch blocks
@Data
public class IfToken extends Token {
    private List<Token> children;
    private List<Token> expressions;
    public IfToken(String value) {
        super(value);
        this.children = new LinkedList<>();
        this.expressions = new LinkedList<>();
    }
    
    @Override
    public String toString() {
        return String.format("=== If: %s === \n", super.getValue());    }
}
