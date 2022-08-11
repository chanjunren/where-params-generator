package com.cjr.parser.tokens;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class ClassToken extends Token {
    
    private List<Token> children;

    public ClassToken(String value) {
        super(value);
        children = new LinkedList<>();
    }
    
    @Override
    public String toString() {
        // StringBuilder sb = new StringBuilder();
        // for (Token t: children) {
        //     sb.append("\t");
        //     sb.append(t.toString());
        //     sb.append(", ");
        // }
        return String.format("=== Class: %s ===\n\t Children: TO BE IMPLEMENTED\n", 
            super.getValue());
    }
 }
