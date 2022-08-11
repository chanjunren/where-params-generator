package com.cjr.parser.components;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.cjr.parser.tokens.ExpresssionToken;

// For now, the goal of this expression parser is to merely parse every expression 
// Does not evaulate value type
// null values, numbers and operators should be excluded
public class ExpressionParser {

    private HashSet<Character> arithemticOperators;
    private HashSet<Character> logicalOperators;
    private HashSet<Character> brackets;
    private static final String NULL_KEYWORD = "null";
    private static final char DOT_OPERATOR = '.';
    private int currIdx;

    public ExpressionParser() {
        initOperatorSets();
    }

    private void initOperatorSets() {
        logicalOperators = new HashSet<>(Arrays.asList('<', '>', '=', '!', '&', '|'));
        arithemticOperators = new HashSet<>(Arrays.asList('+', '-', '*', '/', '%'));
        brackets = new HashSet<>(Arrays.asList('[', ']', '(', ')'));
    }

    public List<ExpresssionToken> parseExpressionOf(String fullExpression) {
        this.currIdx = 0;
        List<ExpresssionToken> expressionTokens = new LinkedList<>();
        while (currIdx < fullExpression.length()) {
            char currChar = fullExpression.charAt(currIdx);
            if (!isSpecialSymbol(currChar) && !isNumber(currChar) && !isWhiteSpace(currChar)) {
                String fullParam = getFullParam(fullExpression);
                if (!isNull(fullParam)) {
                    expressionTokens.add(new ExpresssionToken(fullParam));
                }
            }
            currIdx++;
        }
        return expressionTokens;
    }

    public String getFullParam(String fullExpression) {
        StringBuilder sb = new StringBuilder();
        char currChar = fullExpression.charAt(currIdx);
        while (!isWhiteSpace(currChar) && !isComma(currChar) && !isOperator(currChar) && currIdx + 1 < fullExpression.length()) {
            sb.append(currChar);
            if (currChar == '(') {
                getFullFunctionCall(sb, fullExpression);
                return sb.toString();
            }
            currChar = fullExpression.charAt(++currIdx);
        }
        return sb.toString();
    }

    private void getFullFunctionCall(StringBuilder sb, String fullExpression) {
        while (getNumberOfBracesLeft(sb.toString()) != 0) {
            sb.append(fullExpression.charAt(++currIdx));
        }
        if (currIdx < fullExpression.length() - 1 && fullExpression.charAt(currIdx + 1) == DOT_OPERATOR) {
            currIdx++;
            sb.append(getFullParam(fullExpression));
        }
    }

    private boolean isSpecialSymbol(char c) {
        return isOperator(c) || brackets.contains(c);
    }

    private boolean isOperator(char c) {
        return arithemticOperators.contains(c) || logicalOperators.contains(c);
    }
    
    private boolean isNumber(char c) {
        return Character.isDigit(c);
    }

    private boolean isWhiteSpace(char c) {
        return c == ' ';
    }

    private boolean isComma(char c) {
        return c == ',';
    }

    private boolean isNull(String str) {
        return NULL_KEYWORD.equals(str);
    }

    private int getNumberOfBracesLeft(String stmt) {
        return (int) stmt.chars().filter(c -> c == '(').count()
        -  (int) stmt.chars().filter(c -> c == ')').count();
    }
}
