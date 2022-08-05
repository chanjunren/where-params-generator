package com.cjr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.cjr.tokens.Token;
import com.cjr.tokens.TokenType;

public class SimpleTokeniser {
    private BufferedReader br;
    private StringBuilder sb;
    
    public SimpleTokeniser() {
    }

    public List<Token> tokeniseFile(File inputFile) throws IOException{
        List<Token> tokens = new LinkedList<>();
        this.br = new BufferedReader(new FileReader(inputFile));
        String currLine = br.readLine();
        while (currLine != null) {
            StatementType type = getStatementType(currLine);
            consolidateTokens(tokens, type, currLine);
            currLine = br.readLine();
        }
        return tokens;
    }

    private StatementType getStatementType(String statement) {
        if (isStatement(statement)) {
            System.out.printf("Statement encountered : | %s\n", statement);
            return StatementType.OTHERS;
        } else if (isLoopStatement(statement)) {
            System.out.printf("Loop encountered: %s\n", statement);
            return StatementType.LOOP;
        } else if (isCatch(statement)) {
            System.out.printf("Catch encountered : | %s\n", statement);
            return StatementType.CATCH;  
        } else if (isIfStatement(statement)) {
            System.out.printf("If encountered: %s\n", statement);
            return StatementType.IF;
        } else if (isFunction(statement)) {
            System.out.printf("Function encountered : | %s\n", statement);
            return StatementType.FUNCTION;
        } 

        System.out.printf("Others encountered: %s\n", statement);
        return StatementType.OTHERS;
    }

    
    private void consolidateTokens(List<Token> tokens, StatementType stmtType, String stmt) throws IOException {
        if (stmtType == StatementType.FUNCTION) {
            if (!stmt.contains("{")) {
                sb = new StringBuilder(stmt);
                while (!sb.toString().contains("{")) {
                    sb.append(br.readLine());
                }
            }
            Token nameToken = new Token(getFunctionName(stmt), TokenType.FUNCTION_NAME);
            tokens.add(nameToken);
            tokeniseFunctionBody(tokens);
        } else {
            return;
        }
    }

    private void tokeniseFunctionBody(List<Token> tokens) throws IOException {
        int numberOfCloseBracketsLeft = 1;
        boolean isFunctionBodyTokenised = numberOfCloseBracketsLeft != 0;
        while (!isFunctionBodyTokenised) {
            String stmt = br.readLine();
            if (isIfStatement(stmt)) {
                System.out.println("If statement encountered!");
            } else if (isLoopStatement(stmt)) {
                System.out.println("Loop statement encountered!");
            }
            numberOfCloseBracketsLeft += stmt.chars().filter(c -> (c == '{')).count();
            numberOfCloseBracketsLeft -= stmt.chars().filter(c -> (c == '}')).count();
        }
    }

    private boolean isStatement(String statement) {
        if (statement.contains(";")) return true;
        return false;
    }

    private boolean isFunction(String statement) {
        String functionPattern = "(.*)(\\(.*)";
        
        return statement.matches(functionPattern) && !isLoopStatement(statement) && !isAnnotation(statement) && !isCatch(statement);
    }

    private boolean isIfStatement(String statement) {
        return Arrays.stream(statement.split(" ")).anyMatch(s -> s.equals("if"));
    }

    private boolean isLoopStatement(String statement) {
        String forPattern = "(for).*\\(";
        String whilePattern = "(while)(.*\\()";
        return statement.matches(forPattern) || statement.contains(whilePattern);
    }

    private boolean isAnnotation(String statement) {
        return statement.contains("@");
    }

    private boolean isCatch(String statement) {
        return Arrays.stream(statement.split(" ")).anyMatch(s -> s.equals("catch"));
    }

    private String getFunctionName(String stmt) {
        String[] splitStmt = stmt.split(" ");
        int currIdx = 0;
        String currElement = splitStmt[currIdx];
        while (!currElement.contains("(")) currElement = splitStmt[++currIdx];

        return currElement.substring(0, currElement.indexOf("("));
    }

    // public static void main(String[] args) {
    //     SimpleTokeniser tokeniser = new SimpleTokeniser();
    //     String test = "public static void main(String[] args) {";
    //     System.out.println(tokeniser.isFunction(test));
    // }
}
