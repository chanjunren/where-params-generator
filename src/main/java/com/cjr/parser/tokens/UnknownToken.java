package com.cjr.parser.tokens;

public class UnknownToken extends Token {

    public UnknownToken(String value) {
        super(value);
    }
    
    @Override
    public String toString() {
        return String.format("Unknown: %s", super.getValue());
    }
}
