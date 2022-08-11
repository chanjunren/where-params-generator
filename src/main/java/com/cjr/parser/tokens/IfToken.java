package com.cjr.parser.tokens;

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
        StringBuilder sb = new StringBuilder();
        for (ExpresssionToken et: predicate) {
            sb.append(et.getValue());
            sb.append(" | ");
        }

        if (innerIfTokens.size() != 0) {
            for (IfToken it: innerIfTokens) {
                sb.append(it.toString());
            }
        }
        return (sb.length() == 0 || innerIfTokens.size() != 0) ? sb.toString()
            : sb.substring(0, sb.length() - 3);
    }
}
