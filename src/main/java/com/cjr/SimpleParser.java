// package com.cjr;

// import java.io.BufferedReader;
// import java.io.File;
// import java.io.FileReader;
// import java.io.IOException;
// import java.util.Arrays;
// import java.util.HashSet;
// import java.util.LinkedList;
// import java.util.List;

// import com.cjr.parsed_objects.FunctionObject;
// import com.cjr.parsed_objects.IfObject;
// import com.cjr.tokens.Token;

// public class SimpleParser {
//     private BufferedReader br;

//     public SimpleParser(List<Token> tokens) throws IOException {
//     }
    
//     public List<FunctionObject> parseFile() throws IOException {
//         List<FunctionObject> functions = new LinkedList<>();

//         String currLine = br.readLine();
//         while (currLine != null) {
//             parseStatement(currLine, functions);
//             currLine = br.readLine();
//         }
//         br.close();
//         return functions;
//     }

//     private void parseStatement(String line, List<FunctionObject> functions) {
//         // if (getStatementType(line) == StatementType.FUNCTION) {
//         //     functions.add(parseFunction(line));
//         // }
//     }

//     private FunctionObject parseFunction(String line) {
//         FunctionObject fo = new FunctionObject(getFunctionName(line), new HashSet<>(), new HashSet<>());
//         parseFunctionBody(fo);
//         return fo;
//     }

//     private List<IfObject> parseFunctionBody(FunctionObject functionObject) {
//         List<IfObject> ifObjects = new LinkedList<IfObject>();
//         return ifObjects;
//     }

//     // for statements that span multiple lines
//     private String getUntilEndOfStatement() {
//         return "false";
//     }
// }
