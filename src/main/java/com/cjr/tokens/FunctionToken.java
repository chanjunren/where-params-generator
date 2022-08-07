package com.cjr.tokens;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class FunctionToken extends Token {
    private List<Token> children;

    public FunctionToken(String value) {
        super(value);
        children = new LinkedList<>();
    }

    @Override
    public String toString() {
        return String.format("Function: %s\n", super.getValue());
    }
    
}
