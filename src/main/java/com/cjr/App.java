package com.cjr;

import java.io.File;

public class App 
{

    private static final String FILE_PATH = "/Users/chanjunren/meili/junren.chan_dacs_at_okg.com/109/Documents/Projects/okcoin-otc/okcoin-otc-rest/src/main/java/com/okcoin/exchange/c2c/open/C2COpenApplication.java";
    //    private static final String FILE_PATH = "/Users/chanjunren/meili/junren.chan_dacs_at_okg.com/109/Documents/Projects/okcoin-otc/okcoin-otc-rest/src/main/java/com/okcoin/exchange/c2c/open/rest/controller/v3/V3TradingOrderController.java";
    public static void main( String[] args )
    {
        File inputFile = new File(FILE_PATH);
        SimpleParser simpleParser = new SimpleParser(inputFile);
        List<FunctionObject> functions = simpleParser.parseFile();
        OutputPrinter.print(functions);
    }
}
