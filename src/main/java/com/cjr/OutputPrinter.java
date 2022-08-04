package com.cjr;

import com.cjr.parsed.FunctionObject;

public class OutputPrinter {
    private static final String OUTPUT_DELIMITER = "================================================================";

    public static void print(List<FunctionObject> functions) {
        for (FunctionObject fo: functions) {
            System.out.println(fo);
        }
    }
}
