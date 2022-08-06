package com.cjr.tokens;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class ClassToken extends Token {
    
    private List<Token> childrenTokens;

    public ClassToken(String value) {
        super(value);
        childrenTokens = new LinkedList<>();
    }
    
    @Override
    public String toString() {
        return String.format("Class: %s", super.getValue());
    }
 }
