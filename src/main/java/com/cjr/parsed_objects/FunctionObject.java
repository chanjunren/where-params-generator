package com.cjr.parsed_objects;

import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FunctionObject {
    private String functionName;

    private HashSet<String> functionsCalledSet;
    private HashSet<String> branchVariablesSet;

    @Override
    public String toString() {
        return String.format("Function name: %n\n Functions Called: \n Variables used: \n");
    }
}
