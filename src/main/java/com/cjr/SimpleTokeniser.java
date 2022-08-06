package com.cjr;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.cjr.tokens.AnnotationToken;
import com.cjr.tokens.ClassToken;
import com.cjr.tokens.FunctionToken;
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
            tokens.add(tokeniseFileStatement(inputQueue.poll()));
        }
        return tokens;
    }

    private Token tokeniseFileStatement(String stmt) {
        if (isImport(stmt)) {
            return new StatementToken(stmt);
        } else if (isAnnotation(stmt)) {
            String fullAnnotation = getExpressionBody(stmt);
            System.out.printf("Annotation: %s\n", fullAnnotation);
            return new AnnotationToken(fullAnnotation);
        } else if (isClass(stmt)) {
            ClassToken ct = new ClassToken(stmt);
            System.out.println(ct);
            jumpToOpeningBrace(stmt, '{', '}');
            ct.setChildrenTokens(tokeniseClassBody());
            return ct;
        } else {
            System.err.printf("Unhandled file statement type: %s\n", stmt);
            return new UnknownToken(stmt);
        }
    }

    private List<Token> tokeniseClassBody() {
        Queue<String> body = getBodyOfScope(1);
        System.out.println("=== Start of class body === ");
        for (String bodyStmt: body) {
            System.out.println(bodyStmt);
        }
        System.out.println("=== End of class body");
        List<Token> children = new LinkedList<>();
        
        // while (!body.isEmpty()) {
        //     String curr = body.poll();
        //     children.add(tokeniseClassBodyStmt(body, curr));
        // }
        return children;
    }

    private Token tokeniseClassBodyStmt(Queue<String> body, String curr) {
        if (isStatement(curr)) {
            return new StatementToken(curr);
        } else if (isAnnotation(curr)) {
            String fullAnnotation = getExpressionBody(curr);
            return new AnnotationToken(fullAnnotation);
        } else if (isFunction(curr)) {
            FunctionToken ft = new FunctionToken(getFunctionName(curr));
            jumpToOpeningBrace(curr, '{', '}');
            ft.setChildrenTokens(tokeniseBlockBody());
            return ft;
        } else {
            System.err.println("Unhandled body stmt type");
            return new UnknownToken(curr);
        }
    }


    // for functions / if / try / catch / loop blocks
    private List<Token> tokeniseBlockBody() {
        return new LinkedList<>();
    }

    private Token tokeniseBlockStmt() {
        return new UnknownToken("What");
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
        return stmt.matches("catch( )?\\((.*)\\)");
    }

    private void jumpToOpeningBrace(String stmt, char openingBraceType, char closingBraceType) {
        while (stmt.chars().filter(c -> c == openingBraceType).count() - stmt.chars().filter(c -> c == closingBraceType).count() 
            != 1l) {
            inputQueue.poll();
        }
    }

    private int getNumberOfBracesLeft(String stmt, char openingBraceType, char closingBraceType) {
        return (int) stmt.chars().filter(c -> c == openingBraceType).count()
        -  (int) stmt.chars().filter(c -> c == closingBraceType).count();
    }

    private Queue<String> getBodyOfScope(int numberOfBracesLeft) {
        Queue<String> body = new LinkedList<>();
        // Because opening brace has been jumped
        while (numberOfBracesLeft != 0) {
            String currStmt = inputQueue.poll();
            body.add(currStmt);
            numberOfBracesLeft += getNumberOfBracesLeft(currStmt, '{', '}');
        }
        return body;
    }

    private String getExpressionBody(String currStmt) {
        sb = new StringBuilder(currStmt);
        int numberOfBracesLeft = getNumberOfBracesLeft(currStmt, '(', ')');
        while (numberOfBracesLeft != 0 && currStmt != null) {
            sb.append("\n");
            currStmt = inputQueue.poll();
            sb.append(currStmt);
            numberOfBracesLeft += getNumberOfBracesLeft(currStmt, '(', ')');
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

    // public static void main(String[] args) {
    //     SimpleTokeniser st = new SimpleTokeniser();
    //     System.out.println(st.isIfStatement("if(int a, int b) {"));
    //     System.out.println(st.isIfStatement("if (int a, int b"));
    // }
}
