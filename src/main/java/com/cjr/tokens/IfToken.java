package com.cjr.tokens;

// For while / if / catch blocks
public class IfToken extends Token {
    public IfToken(String value) {
        super(value);
    }
    
    @Override
    public String toString() {
        return String.format("If: %s", super.getValue());
    }
}
