package com.cjr.parser.tokens;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

// For while / if / catch blocks
@Data
public class IfToken extends Token {

    private List<IfToken> innerIfTokens;
    private List<CheckBizToken> checkBizTokens;
    private List<ExpresssionToken> predicate;
    
    public IfToken(String value) {
        super(value);
        this.innerIfTokens = new LinkedList<>();
        this.checkBizTokens = new LinkedList<>();
        this.predicate = new LinkedList<>();
    }

    public void addChild(IfToken newIfToken) {
        this.innerIfTokens.add(newIfToken);
    }

    public void addChild(CheckBizToken newCheckBizToken) {
        this.checkBizTokens.add(newCheckBizToken);
    }

    @Override
    public String toString() {
        StringBuilder mainSb = new StringBuilder();
        appendExpression(mainSb);
        appendInnerIfTokens(mainSb);
        appendCheckBizTokens(mainSb);
        
        return mainSb.toString();
    }

    public void appendExpression(StringBuilder sb) {
        for (ExpresssionToken et: predicate) {
            sb.append(et.getValue());
            sb.append(" | ");
        }
        sb.delete(sb.length() - 3, sb.length());
    }

    private void appendInnerIfTokens(StringBuilder sb) {
        if (innerIfTokens.size() != 0) {
            for (IfToken it: innerIfTokens) {
                sb.append(it.toString());
                sb.append(" | ");
            }
            sb.delete(sb.length() - 3, sb.length());
        }
    }

    private void appendCheckBizTokens(StringBuilder sb) {
        if (checkBizTokens.size() != 0) {
            sb.append("\n");
            for (CheckBizToken bt: checkBizTokens) {
                sb.append(bt.toString());
                sb.append(" | ");
            }
            sb.delete(sb.length() - 3, sb.length());
        }
    }
}
