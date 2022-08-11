package com.cjr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import com.cjr.tokens.ClassToken;

public class App {

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
    }
}
