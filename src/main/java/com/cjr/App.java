package com.cjr;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.cjr.tokens.Token;

public class App 
{

    private static final String FILE_PATH = "/Users/chanjunren/Projects/test-fields-generator/src/main/java/com/cjr/tests/V3TradingOrderController.txt";
    //    private static final String FILE_PATH = "/Users/chanjunren/meili/junren.chan_dacs_at_okg.com/109/Documents/Projects/okcoin-otc/okcoin-otc-rest/src/main/java/com/okcoin/exchange/c2c/open/rest/controller/v3/V3TradingOrderController.java";
    public static void main( String[] args )
    {
        File inputFile = new File(FILE_PATH);
        SimpleTokeniser tokeniser = new SimpleTokeniser();
        try {
            List<Token> tokens = tokeniser.tokeniseFile(inputFile);
            // for (Token t: tokens) {
            //     System.out.println(t);
            // }
        } catch (IOException e) {
            System.err.println(e);
        } 
        // List<FunctionObject> functions = simpleParser.parseFile();
        // OutputPrinter.print(functions);
    }
}
