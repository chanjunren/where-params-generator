package com.cjr.tokens;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class Token {
    private String value;
    private TokenType type;

    @Override
    public String toString() {
        return type + ": " + value;
    }

}
