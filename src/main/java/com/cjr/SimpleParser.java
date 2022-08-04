package com.cjr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.cjr.parsed_objects.FunctionObject;
import com.cjr.parsed_objects.IfObject;

public class SimpleParser {
    private HashSet<String> ;
    private BufferedReader br;


    public SimpleParser(File inputFile) throws IOException {
        this.br = new BufferedReader(new FileReader(inputFile));
        accessModifiers = new HashSet<>(Arrays.asList(new String[]{"public", "private", "protected"}));
    }
    
    public List<FunctionObject> parseFile() throws IOException {
        List<FunctionObject> functions = new LinkedList<>();

        String currLine = br.readLine();
        while (currLine != null) {
            parseStatement(currLine, functions);
            currLine = br.readLine();
        }
        br.close();
        return functions;
    }

    private void parseStatement(String line, List<FunctionObject> functions) {
        if (getStatementType(line) == StatementType.FUNCTION) {
            functions.add(parseFunction(line));
        }
    }

    private FunctionObject parseFunction(String line) {
        FunctionObject fo = new FunctionObject(getFunctionName(line), new HashSet<>(), new HashSet<>());
        parseFunctionBody(fo);
        return fo;
    }

    private List<IfObject> parseFunctionBody(FunctionObject functionObject) {
        List<IfObject> ifObjects = new LinkedList<IfObject>();
        return ifObjects;
    }

    private StatementType getStatementType(String statement) {
        if (isFunctionStatement(statement)) {
            return StatementType.FUNCTION;
        } else if (isIfStatement(statement)) {
            return StatementType.IF;
        } else {
            return StatementType.OTHERS;
        }
    }

    private boolean isFunctionStatement(String statement) {
        String functionPattern = "\\(.*";
        return statement.matches(functionPattern) && !isLoopStatement(statement);
    }

    private boolean isIfStatement(String statement) {
        String ifPattern = "(if).*\\(";
        return statement.contains(ifPattern);
    }

    private boolean isLoopStatement(String statement) {
        String forPattern = "(for).*\\(";
        String whilePattern = "(while).*\\(";
        return statement.matches(forPattern) || statement.contains(whilePattern);
    }

    private String getFunctionName(String stmt) {
        String[] splitStmt = stmt.split(" ");
        int currIdx = 0;
        String currElement = splitStmt[currIdx];
        while (!currElement.contains("(")) currElement = splitStmt[++currIdx];

        return currElement.substring(0, currElement.indexOf("("));
    }

    // for statements that span multiple lines
    private String getUntilEndOfStatement() {
        return "false";
    }



}
