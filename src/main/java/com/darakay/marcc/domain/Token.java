package com.darakay.marcc.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

@EqualsAndHashCode
@ToString
public class Token {
    private String tagValue;
    private boolean isTerm;

    public Token(String value, Map<String, String> attrs, boolean isTerm, boolean isClosed) {
        this.isTerm = isTerm;
        if(attrs.isEmpty()) {
            this.tagValue = buildTag(value, isTerm, isClosed);
        } else {
            this.tagValue = buildTagWithAttributes(value, attrs, isTerm, isClosed);
        }
    }

    private String buildTagWithAttributes(String value, Map<String, String> attrs, boolean isTerm, boolean isClosed){
        StringBuilder stringBuilder = new StringBuilder();
        attrs.forEach((k, v) -> stringBuilder.append(k).append("=").append("\"").append(v).append("\" "));
        return buildTag(value + " " + stringBuilder.toString(), isTerm, isClosed);
    }

    private String buildTag(String value, boolean isTerm, boolean isClosed){
        if(!isTerm) {
            return value;
        }
        if(isClosed) {
            return "<" + value + "/>";
        }
        return "<" + value + ">";
    }
}
