package com.cjr.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import com.cjr.parser.components.SimpleTokeniser;
import com.cjr.parser.tokens.ClassToken;
import com.cjr.parser.tokens.Token;

public class FileParser {

    private static final String FILE_PATH = "/Users/chanjunren/Projects/test-fields-generator/src/main/java/com/cjr/tests/V3TradingOrderController.txt";
    // private static final String FILE_PATH = "/Users/chanjunren/Projects/test-fields-generator/src/main/java/com/cjr/tests/C2COpenApplication.txt";
   
    
    public static void main( String[] args ) {
        Queue<String> inputFileStrings = new LinkedList<>();

        try {
            File inputFile = new File(FILE_PATH);
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            String currStmt = br.readLine();
            while (currStmt != null) {
                if (currStmt.trim().length() != 0) {
                    inputFileStrings.add(currStmt.trim());    
                }
                currStmt = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.err.println(e);
        }
        SimpleTokeniser tokeniser = new SimpleTokeniser(inputFileStrings);
        ClassToken ct = tokeniser.extractClassToken();
        for (Token t: ct.getChildren()) {
            System.out.println(t.toString());
            System.out.println();
        }
    }
}
