package com.cjr;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.cjr.tokens.AnnotationToken;
import com.cjr.tokens.ClassToken;
import com.cjr.tokens.FunctionToken;
import com.cjr.tokens.IfToken;
import com.cjr.tokens.LoopToken;
import com.cjr.tokens.StatementToken;
import com.cjr.tokens.Token;
import com.cjr.tokens.UnknownToken;

public class SimpleTokeniser {
    private StringBuilder sb;
    private Queue<String> inputQueue;

    public SimpleTokeniser(Queue<String> inputQueue) {
        this.inputQueue = inputQueue;
    }

    public List<Token> tokeniseFile() {
        List<Token> tokens = new LinkedList<>();        
        while (!inputQueue.isEmpty()) {
            tokens.add(tokeniseFileStatement(inputQueue.poll(), inputQueue));
        }
        return tokens;
    }

    private Token tokeniseFileStatement(String stmt, Queue<String> fileBody) {
        if (isImport(stmt)) {
            return new StatementToken(stmt);
        } else if (isAnnotation(stmt)) {
            Queue<String> fullAnnotation = getLinesForFullExpression(stmt, fileBody);
            sb = new StringBuilder();
            for (String s: fullAnnotation) {
                sb.append(s);
                sb.append("\n");
            }
            return new AnnotationToken(sb.toString());
        } else if (isClass(stmt)) {
            ClassToken ct = new ClassToken(stmt);
            System.out.println(ct);
            jumpToOpeningBrace(stmt, fileBody);
            ct.setChildren(tokeniseClassBody(fileBody));
            return ct;
        } else {
            System.err.printf("Unhandled file statement type: %s\n", stmt);
            return new UnknownToken(stmt);
        }
    }

    private List<Token> tokeniseClassBody(Queue<String> fileBody) {
        Queue<String> body = getBodyOfScope(1, fileBody);
        List<Token> children = new LinkedList<>();
        
        while (!body.isEmpty()) {
            String curr = body.poll();
            children.add(tokeniseClassBodyStmt(body, curr));
        }
        return children;
    }

    private Token tokeniseClassBodyStmt(Queue<String> body, String curr) {
        if (isAnnotation(curr)) {
            System.out.printf("Class annotation: %s\n", curr);
            return new AnnotationToken(curr);
        } else if (isStatement(curr)) {
            System.out.printf("Class body stmt: %s\n", curr);
            return new StatementToken(curr);
        } else if (isAnnotation(curr)) {
            Queue<String> fullAnnotation = getLinesForFullExpression(curr, body);
            sb = new StringBuilder();
            for (String s: fullAnnotation) {
                sb.append(s);
                sb.append("\n");
            }
            System.out.printf("Class body annotation: %s\n", sb.toString());
            return new AnnotationToken(sb.toString());
        } else if (isFunction(curr)) {
            System.out.printf("Class body fn: %s\n", curr);
            FunctionToken ft = new FunctionToken(getFunctionName(curr));
            jumpToOpeningBrace(curr, body);
            ft.setChildren(tokeniseBlockBody(body));
            return ft;
        } else {
            System.out.printf("Unhandled class body stmt type: %s\n", curr);
            return new UnknownToken(curr);
        }
    }


    // for functions / if / try / catch / loop blocks
    private List<Token> tokeniseBlockBody(Queue<String> body) {
        System.out.println("Tokenising block body");
        List<Token> block = new LinkedList<>();
        while (!body.isEmpty()) {
            String currStmt = body.poll();
            block.add(tokeniseBlockStmt(currStmt, body));
        }
        return block;
    }

    private Token tokeniseBlockStmt(String stmt, Queue<String> body) {
        if (isAnnotation(stmt)) {
            System.out.printf("Annotation: %s\n", stmt);
            return new AnnotationToken(stmt);
        } else if (isStatement(stmt)) {
            System.out.printf("Statement: %s\n", stmt);
            return new StatementToken(stmt);
        } else if (isIfStatement(stmt)) {
            System.out.printf("If Statement: %s\n", stmt);
            return tokeniseIfSttmt(stmt, body);
        } else if (isLoop(stmt)) { 
            System.out.printf("Loop Statement: %s\n", stmt);
            return tokeniseLoopToken(stmt, body);
        } else if (isCatch(stmt))  {
            System.out.printf("Catch Statement: %s\n", stmt);
            return new UnknownToken(stmt);
        }
        else {
            System.err.printf("Unhandled block stmt: %s\n", stmt);
            return new UnknownToken(stmt);
        }
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
        return stmt.matches("(public|private|protected)? ?(static)? ?(.*) (.*) ?(\\()(.*)");
    }

    private boolean isIfStatement(String stmt) {
        return stmt.matches("(if)( )?\\((.*)");
    }

    private boolean isLoop(String stmt) {
        String forPattern = "(for)( )?\\((.*)";
        String whilePattern = "(while)( )?\\((.*)";
        return stmt.matches(forPattern) || stmt.matches(whilePattern);
    }

    private boolean isAnnotation(String stmt) {
        return stmt.matches("@(.*)");
    }

    // Assuming catch statemnet only consists of one line
    private boolean isCatch(String stmt) {
        return stmt.matches("(\\}?\\ ?)catch(\\ )?\\((.*)\\) ?\\{");
    }

    private void jumpToOpeningBrace(String stmt, Queue<String> body) {
        String curr = stmt;
        while (getNumberOfBracesLeft(curr, '{', '}') != 1) {
            curr = body.poll();
        }
    }

    private int getNumberOfBracesLeft(String stmt, char openingBraceType, char closingBraceType) {
        return (int) stmt.chars().filter(c -> c == openingBraceType).count()
        -  (int) stmt.chars().filter(c -> c == closingBraceType).count();
    }

    private Queue<String> getBodyOfScope(int numberOfBracesLeft, Queue<String> body) {
        Queue<String> bodyList = new LinkedList<>();
        // Because opening brace has been jumped
        while (numberOfBracesLeft != 0) {
            String currStmt = body.poll();
            bodyList.add(currStmt);
            numberOfBracesLeft += getNumberOfBracesLeft(currStmt, '{', '}');
        }
        return bodyList;
    }

    // For annotations outside of a class (does not handle inner class)
    private Queue<String> getLinesForFullExpression(String currStmt, Queue<String> body) {
        Queue<String> allLines = new LinkedList<>();
        allLines.add(currStmt);
        int numberOfBracesLeft = getNumberOfBracesLeft(currStmt, '(', ')');
        while (numberOfBracesLeft != 0 && currStmt != null) {
            currStmt = body.poll();
            allLines.add(currStmt);
            numberOfBracesLeft += getNumberOfBracesLeft(currStmt, '(', ')');
        }
        return allLines;
    }

    private String getFullExpression(Queue<String> body) {
        String curr = body.poll();
        sb = new StringBuilder(curr);
        deleteCharUntilStartOfExpression(sb);
        deleteCharUntilEndOfExpression(sb);
        return sb.toString();
    }

    private void deleteCharUntilStartOfExpression(StringBuilder sb) {
        while (sb.charAt(0) != '(') {
            sb.deleteCharAt(0);
        }
    }

    private void deleteCharUntilEndOfExpression(StringBuilder sb) {
        while (sb.charAt(sb.length() - 1) != ')') {
            sb.deleteCharAt(sb.length() - 1);
        }
    }
    
    // Get expression string
    // Parse expression string into tokens
    // Parse children 
    private IfToken tokeniseIfSttmt(String stmt, Queue<String> body) {
        IfToken it = new IfToken(stmt);
        Queue<String> fullExpressionList = getLinesForFullExpression(stmt, body);
        for (String s: fullExpressionList) {
            System.out.printf("Expression in list: %s\n", s);
        }
        String fullExpression = getFullExpression(fullExpressionList);
        it.setExpressionString(fullExpression);
        it.setChildren(tokeniseBlockBody(body));
        return new IfToken(stmt);
    }

    // Get expression string
    // Parse expression string into tokens
    // Parse children 
    private LoopToken tokeniseLoopToken(String stmt, Queue<String> body) {
        LoopToken lt = new LoopToken(stmt);
        Queue<String> fullExpressionList = getLinesForFullExpression(stmt, body);
        String fullExpression = getFullExpression(fullExpressionList);
        lt.setExpressionString(fullExpression);
        lt.setChildren(tokeniseBlockBody(body));
        return lt;
    }
    
    private String getFunctionName(String stmt) {
        String[] splitStmt = stmt.split(" ");
        int currIdx = 0;
        String currElement = splitStmt[currIdx];
        while (!currElement.contains("(")) currElement = splitStmt[++currIdx];
        return currElement.substring(0, currElement.indexOf("("));
    }
}
