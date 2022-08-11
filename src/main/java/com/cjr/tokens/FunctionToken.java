package com.cjr.tokens;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class FunctionToken extends Token {
    private List<IfToken> ifTokens;

    public FunctionToken(String value) {
        super(value);
        ifTokens = new LinkedList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (IfToken it: ifTokens) {
            sb.append(it.toString());
            sb.append(" | ");
        }
        return String.format("Function: %s\n%s", 
            super.getValue(), (sb.length() == 0) || (ifTokens.size() == 0) ? sb.toString() 
                : sb.substring(0, sb.length() - 3));
    }
    
}
