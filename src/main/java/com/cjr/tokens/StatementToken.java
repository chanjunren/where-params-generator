package com.cjr.tokens;

import lombok.Data;

@Data
public class StatementToken extends Token {

    public StatementToken(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return String.format("Statement: %s\n", super.getValue());
    }
    
}
