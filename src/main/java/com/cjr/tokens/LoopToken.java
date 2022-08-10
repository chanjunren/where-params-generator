package com.cjr.tokens;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;
@Data
public class LoopToken extends Token {
    private List<Token> children;
    private List<Token> expressions;
    private String childrenBody;
    private String expressionString;
    
    public LoopToken(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return String.format("Loop: %s", super.getValue());
    }
    
}
