package com.cjr;

public enum StatementType {
    // Typical lines you come across before hitting functions
    IMPORT, CLASS, ANNOTATION,
    // Class body (functions, assignments etc.)
    FUNCTION, OTHERS,
    // Function Body
    IF, LOOP, CATCH, BIZ,
    UNKNOWN
}