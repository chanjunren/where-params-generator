package com.cjr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import com.cjr.tokens.AnnotationToken;
import com.cjr.tokens.FunctionToken;
import com.cjr.tokens.StatementToken;
import com.cjr.tokens.Token;

public class SimpleTokeniser {
    private BufferedReader br;
    private StringBuilder sb;
    
    public SimpleTokeniser() {
    }

    public List<Token> tokeniseFile(File inputFile) throws IOException {
        List<Token> tokens = new LinkedList<>();
        
        this.br = new BufferedReader(new FileReader(inputFile));
        String currLine = br.readLine();
        while (currLine != null) {
            tokens.add(tokeniseFileStatement(currLine));
            currLine = br.readLine();
        }
        return tokens;
    }

    private Token tokeniseFileStatement(String stmt) throws IOException {
        if (isImport(stmt)) {
            System.out.printf("Import: %s\n", stmt);
            return new StatementToken(stmt);
        } else if (isAnnotation(stmt)) {
            System.out.printf("Annotation: %s\n", stmt);
            return new AnnotationToken(stmt);
        } else if (isClass(stmt)) {
            System.out.printf("Class: %s\n", stmt);
            FunctionToken ft = new FunctionToken(stmt);
            jumpToOpeningBrace(stmt, '{', '}');
            ft.setChildrenTokens(tokeniseBodyWithBraces('{', '}'));
            return ft;
        } else {
            System.err.printf("Unhandled file statement type: %s\n", stmt);
            return null;
        }
    }

    private List<Token> tokeniseBodyWithBraces(char openingBraceType, char closingBraceType) throws IOException {
        String stmtBody = getBody(openingBraceType, closingBraceType);
        System.out.printf(" === BODY === %s\n === END ===\n", stmtBody);
        List<Token> tokens = new LinkedList<>();
        // Tokenise...

        return tokens;
    }

    private boolean isClass(String stmt) {
        return stmt.matches("(public|private|protected)? (class) (.+)\\{");
    }

    private boolean isImport(String stmt) {
        return stmt.matches("(import) (.*)(;)");
    }

    private boolean isStatement(String stmt) {
        return stmt.contains(";");
    }

    private boolean isFunction(String stmt) {
        // access modifier, static?, return_type, name, arguments
        return stmt.matches("(public|private|protected)? (static)? (.*) (.*) (\\()");
    }

    private boolean isIfStatement(String stmt) {
        return Arrays.stream(stmt.split(" ")).anyMatch(s -> s.equals("if"));
    }

    private boolean isLoop(String stmt) {
        String forPattern = "(for) \\(";
        String whilePattern = "(while)( \\()";
        return stmt.matches(forPattern) || stmt.matches(whilePattern);
    }

    private boolean isAnnotation(String stmt) {
        return stmt.matches("@(.*)");
    }

    private boolean isCatch(String stmt) {
        return stmt.matches("catch \\((.*)\\)");
    }

    private void jumpToOpeningBrace(String stmt, char openingBraceType, char closingBraceType) throws IOException {
        while (stmt.chars().filter(c -> c == openingBraceType).count() - stmt.chars().filter(c -> c == closingBraceType).count() 
            != 1l) {
            br.readLine();
        }
    }

    private int getNumberOfBracesLeft(String stmt, char openingBraceType, char closingBraceType) {
        return (int) stmt.chars().filter(c -> c == openingBraceType).count()
        -  (int) stmt.chars().filter(c -> c == closingBraceType).count();
    }

    private String getBody(char openingBraceType, char closingBraceType) throws IOException {
        String currStmt = br.readLine();
        sb = new StringBuilder(currStmt);
        int numberOfBracesLeft = getNumberOfBracesLeft(currStmt, openingBraceType, closingBraceType);
        // Because opening brace has been jumped
        while (numberOfBracesLeft != -1 && currStmt != null) {
            sb.append("\n");
            sb.append(currStmt);
            numberOfBracesLeft += getNumberOfBracesLeft(currStmt, openingBraceType, closingBraceType);
            currStmt = br.readLine();
        }
        return sb.toString();
    }
    
    private String getFunctionName(String stmt) {
        String[] splitStmt = stmt.split(" ");
        int currIdx = 0;
        String currElement = splitStmt[currIdx];
        while (!currElement.contains("(")) currElement = splitStmt[++currIdx];

        return currElement.substring(0, currElement.indexOf("("));
    }

    // private String getFullExpression() {

    // }

    // private String getCommentBody() {

    // }

    // public static void main(String[] args) {
    //     SimpleTokeniser tokeniser = new SimpleTokeniser();
    //     String test = "public static void main(String[] args) {";
    //     System.out.println(tokeniser.isFunction(test));
    // }
}
