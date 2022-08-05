package com.cjr.tokens;

public class LoopToken extends Token {
    public LoopToken(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return String.format("Loop: %s", super.getValue());
    }
    
}
