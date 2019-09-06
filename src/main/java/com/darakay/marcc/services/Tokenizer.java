package com.darakay.marcc.services;

import com.darakay.marcc.domain.Token;
import com.google.common.collect.ImmutableMap;
import org.commonmark.internal.util.Escaping;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Tokenizer {
    private List<Token> tokens;

    public Tokenizer(){
        this.tokens = new ArrayList<>();
    }

    List<Token> getTokens(){
        return tokens;
    }

    void openTag(String tagName, Map<String, String> attr) {
        tokens.add(new Token(tagName, attr, true, false));
    }

    void openTagWithoutAttrs(String tagName){
        openTag(tagName, ImmutableMap.of());
    }

    void closeTag(String tagName) {
        tokens.add(new Token(tagName, ImmutableMap.of(), true, true));
    }

    void selfClosedTag(String tagName, Map<String, String> attrs){
        tokens.add(new Token(tagName, attrs, true, true));
    }

    void text(String literal) {
        tokens.add(new Token(literal, ImmutableMap.of(), false, false));
    }

    void raw(String literal){
        tokens.add(new Token(Escaping.escapeHtml(literal), ImmutableMap.of(), false, false));
    }
}
