package com.cjr.parser.tokens.unused;

import com.cjr.parser.tokens.Token;

public class CommentToken extends Token {

    public CommentToken(String value) {
        super(value);
    }
    
    @Override
    public String toString() {
        return String.format("Commment: %s", super.getValue());
    }
}
