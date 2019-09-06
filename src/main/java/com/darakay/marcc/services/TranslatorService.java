package com.darakay.marcc.services;

import com.darakay.marcc.domain.Token;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranslatorService {
    private final Parser parser;

    public TranslatorService(Parser parser) {
        this.parser = parser;
    }

    public List<Token> translate(String value) {
        HtmlTokenVisitor visitor = new HtmlTokenVisitor();
        Node root = parser.parse(value);
        root.accept(visitor);
        return visitor.getTokens();
    }
}
