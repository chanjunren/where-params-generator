package com.cjr;

import java.util.regex.Pattern;

public class ExpressionParser {
    
    public boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile(RegexConstants.NUMERIC_REGEX);
        if (strNum == null) {
            return false; 
        }
        return pattern.matcher(strNum).matches();
    }
}
