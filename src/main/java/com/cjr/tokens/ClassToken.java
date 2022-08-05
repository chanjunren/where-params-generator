package com.cjr.tokens;

public class ClassToken extends Token {

    public ClassToken(String value) {
        super(value);
    }
    
    @Override
    public String toString() {
        return String.format("Class: %s", super.getValue());
    }
 }
