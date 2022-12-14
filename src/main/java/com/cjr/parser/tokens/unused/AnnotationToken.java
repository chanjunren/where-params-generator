package com.cjr.parser.tokens.unused;

import com.cjr.parser.tokens.Token;

public class AnnotationToken extends Token {

    public AnnotationToken(String value) {
        super(value);
    }
    
    @Override
    public String toString() {
        return String.format("Annotation: %s", super.getValue());
    }
}
