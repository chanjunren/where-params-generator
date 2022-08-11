package com.cjr.tokens;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

// For while / if / catch blocks
@Data
public class IfToken extends Token {

    private List<IfToken> innerIfTokens;
    private List<ExpresssionToken> predicate;
    
    public IfToken(String value) {
        super(value);
        this.innerIfTokens = new LinkedList<>();
        this.predicate = new LinkedList<>();
    }
    
    @Override
    public String toString() {
        return String.format("=== If: %s === \n", super.getValue());    }
}
