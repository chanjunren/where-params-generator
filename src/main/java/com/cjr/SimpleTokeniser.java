package com.cjr;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.cjr.tokens.ClassToken;
import com.cjr.tokens.ExpresssionToken;
import com.cjr.tokens.FunctionToken;
import com.cjr.tokens.IfToken;
import com.cjr.tokens.Token;

public class SimpleTokeniser {
    private static final boolean PRINT_DEBUG = false;
    private StringBuilder sb;
    private Queue<String> inputQueue;
    private ExpressionParser expressionParser;

    public SimpleTokeniser(Queue<String> inputQueue) {
        this.inputQueue = inputQueue;
        this.expressionParser = new ExpressionParser();
    }

    public ClassToken extractClassToken() {
        String currStmt = inputQueue.poll();
        while (!isClass(currStmt)) {
            currStmt = inputQueue.poll();
        }
        return generateClassTokenFrom(currStmt, inputQueue);
    }

    private List<Token> extractChildrenOfClass(Queue<String> fileBody) {
        Queue<String> body = getBodyOfScope(1, fileBody);
        List<Token> children = new LinkedList<>();
    
        String currStmt = body.poll();
        while (currStmt != null) {
            if (isFunction(currStmt)) {
                children.add(generateFunctionTokenFrom(currStmt, body));
            } else if (isClass(currStmt)) {
                children.add(generateClassTokenFrom(currStmt, body));
            }
            currStmt = body.poll();
        }
        return children;
    }



    private List<IfToken> extractIfTokens(Queue<String> body) {
        List<IfToken> ifTokens = new LinkedList<>();
        String currStmt = body.poll();
        while (currStmt != null) {
            if (isIfStatement(currStmt)) {
                ifTokens.add(generateIfTokenFrom(currStmt, body));
            }
            currStmt = body.poll();
        }
        return ifTokens;
    }



    private boolean isClass(String stmt) {
        return stmt.matches(RegexConstants.CLASS_PATTERN);
    }



    private boolean isFunction(String stmt) {
        return stmt.matches(RegexConstants.FUNCTION_PATTERN);
    }

    private boolean isIfStatement(String stmt) {
        return stmt.matches(RegexConstants.IF_PATTERN);
    }

    // Assuming catch statemnet only consists of one line
    private boolean isCatch(String stmt) {
        return stmt.matches(RegexConstants.CATCH_PATTERN);
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
    


    private ClassToken generateClassTokenFrom(String stmt, Queue<String> body) {
        if (PRINT_DEBUG) {
            System.out.printf("Generating CLASS token from: %s\n", stmt);
        }
        ClassToken ct = new ClassToken(stmt);
        jumpToOpeningBrace(stmt, body);
        ct.setChildren(extractChildrenOfClass(body));
        return ct;
    }
    
    private FunctionToken generateFunctionTokenFrom(String stmt, Queue<String> body) {
        if (PRINT_DEBUG) {
            System.out.printf("Generating FUNCTION token from: %s\n", stmt);
        }
        FunctionToken ft = new FunctionToken(getFunctionName(stmt));
        jumpToOpeningBrace(stmt, body);
        Queue<String> functionBody = getBodyOfScope(1, body);
        ft.setIfTokens(extractIfTokens(functionBody));
        return ft;
    }
    
   
    private IfToken generateIfTokenFrom(String stmt, Queue<String> body) {
        if (PRINT_DEBUG) {
            System.out.printf("Generating IF token from: %s\n", stmt);
        }
        IfToken it = new IfToken(stmt);
        Queue<String> fullExpressionList = getLinesForFullExpression(stmt, body);
        String fullExpression = getFullExpression(fullExpressionList);
        if (PRINT_DEBUG) {
            System.out.print("\t");
            for (ExpresssionToken et: expressionParser.parseExpressionOf(fullExpression)) {
                System.out.printf("%s | ", et.getValue());
            }
            System.out.println("\n");
        }
        it.setPredicate(expressionParser.parseExpressionOf(fullExpression));
        it.setInnerIfTokens(extractIfTokens(body));
        return it;
    }
    
    private String getFunctionName(String stmt) {
        String[] splitStmt = stmt.split(" ");
        int currIdx = 0;
        String currElement = splitStmt[currIdx];
        while (!currElement.contains("(")) currElement = splitStmt[++currIdx];
        return currElement.substring(0, currElement.indexOf("("));
    }

    // ======== OLD LOGIC ==============
        // public List<Token> tokeniseFile() {
    //     List<Token> tokens = new LinkedList<>();        
    //     while (!inputQueue.isEmpty()) {
    //         tokens.add(tokeniseFileStatement(inputQueue.poll(), inputQueue));
    //     }
    //     return tokens;
    // }

    // private Token tokeniseFileStatement(String stmt, Queue<String> fileBody) {
    //     if (isImport(stmt)) {
    //         return new StatementToken(stmt);
    //     } else if (isAnnotation(stmt)) {
    //         return generateAnnotationTokenFrom(stmt, fileBody);
    //     } else if (isClass(stmt)) {
    //         return generateClassTokenFrom(stmt, fileBody);
    //     } else {
    //         return new UnknownToken(stmt);
    //     }
    // }


    // private List<Token> tokeniseClassBody(Queue<String> fileBody) {
    //     Queue<String> body = getBodyOfScope(1, fileBody);
    //     List<Token> children = new LinkedList<>();
        
    //     while (!body.isEmpty()) {
    //         String curr = body.poll();
    //         children.add(tokeniseClassBodyStmt(curr, body));
    //     }
    //     return children;
    // }

        // private Token tokeniseClassBodyStmt(String curr, Queue<String> body) {
    //     if (isAnnotation(curr)) {
    //         return new AnnotationToken(curr);
    //     } else if (isStatement(curr)) {
    //         return new StatementToken(curr);
    //     } else if (isAnnotation(curr)) {
    //         return generateAnnotationTokenFrom(curr, body);
    //     } else if (isFunction(curr)) {
    //         return generateFunctionTokenFrom(curr, body);
    //     } else {
    //         return new UnknownToken(curr);
    //     }
    // }


    // for functions / if / try / catch / loop blocks
    // private List<Token> tokeniseBlockBody(Queue<String> body) {
    //     List<Token> block = new LinkedList<>();
    //     while (!body.isEmpty()) {
    //         String currStmt = body.poll();
    //         block.add(tokeniseBlockStmt(currStmt, body));
    //     }
    //     return block;
    // }

    // private Token tokeniseBlockStmt(String stmt, Queue<String> body) {
    //     if (isAnnotation(stmt)) {
    //         return new AnnotationToken(stmt);
    //     } else if (isStatement(stmt)) {
    //         return new StatementToken(stmt);
    //     } else if (isIfStatement(stmt)) {
    //         return generateIfTokenFrom(stmt, body);
    //     } else if (isLoop(stmt)) { 
    //         return generateLoopTokenfrom(stmt, body);
    //     } else if (isCatch(stmt))  {
    //         return new UnknownToken(stmt);
    //     } else {
    //         return new UnknownToken(stmt);
    //     }
    // }

    // private ClassToken generateClassTokenFrom(String stmt, Queue<String> body) {
    //     if (PRINT_DEBUG) {
    //         System.out.printf("Generating CLASS token from: %s\n", stmt);
    //     }
    //     ClassToken ct = new ClassToken(stmt);
    //     jumpToOpeningBrace(stmt, body);
    //     ct.setChildren(tokeniseClassBody(body));
    //     return ct;
    // }

    // private boolean isLoop(String stmt) {    
    //     return stmt.matches(RegexConstants.FOR_PATTERN) || stmt.matches(RegexConstants.WHILE_PATTERN);
    // }

    // private boolean isAnnotation(String stmt) {
    //     return stmt.matches(RegexConstants.ANNOTATION_PATTERN);
    // }

    // private boolean isImport(String stmt) {
    //     return stmt.matches(RegexConstants.IMPORT_PATTERN);
    // }

    // private boolean isStatement(String stmt) {
    //     return stmt.contains(";");
    // }

    // private AnnotationToken generateAnnotationTokenFrom(String stmt, Queue<String> body) {
    //     Queue<String> fullAnnotation = getLinesForFullExpression(stmt, body);
    //     sb = new StringBuilder();
    //     for (String s: fullAnnotation) {
    //         sb.append(s);
    //         sb.append("\n");
    //     }
    //     return new AnnotationToken(sb.toString());
    // }

    // private LoopToken generateLoopTokenfrom(String stmt, Queue<String> body) {
    //     if (PRINT_DEBUG) {
    //         System.out.printf("Generating Loop token from: %s\n", stmt);
    //     }
    //     LoopToken lt = new LoopToken(stmt);
    //     Queue<String> fullExpressionList = getLinesForFullExpression(stmt, body);
    //     String fullExpression = getFullExpression(fullExpressionList);
    //     lt.setExpressionString(fullExpression);
    //     lt.setChildren(tokeniseBlockBody(body));
    //     return lt;
    // }
}
