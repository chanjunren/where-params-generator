package com.cjr.parser.components;

public class RegexConstants {
    // For tokenising
    public static final String FOR_PATTERN = "(for)( )?\\((.*)";
    public static final String WHILE_PATTERN = "(while)( )?\\((.*)";
    public static final String IF_PATTERN = "(if)( )?\\((.*)";
    public static final String FUNCTION_PATTERN = "(public|private|protected)? ?(static)? ?(.*) (.*) ?(\\()(.*)";
    public static final String CLASS_PATTERN = "(public|private|protected)? (class) (.+)\\{";
    public static final String IMPORT_PATTERN = "(import) (.*)(;)";
    public static final String CATCH_PATTERN = "(\\}?\\ ?)catch(\\ )?\\((.*)\\) ?\\{";
    public static final String ANNOTATION_PATTERN = "@(.*)";
    
    public static final String CHECK_BIZ_PATTERN = "(.*)(checkBiz)(.*)";

    // For expression parsing
    public static final String NUMERIC_REGEX = "-?\\d+(\\.\\d+)?";
}
