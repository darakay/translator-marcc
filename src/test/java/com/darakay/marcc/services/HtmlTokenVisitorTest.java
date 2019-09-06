package com.darakay.marcc.services;

import com.darakay.marcc.config.TranslatorConfiguration;
import com.darakay.marcc.domain.Token;
import com.google.common.collect.ImmutableMap;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(TranslatorConfiguration.class)
public class HtmlTokenVisitorTest {

    private HtmlTokenVisitor htmlTokenVisitor;

    @Autowired
    private Parser parser;

    @Before
    public void setUp(){
        htmlTokenVisitor = new HtmlTokenVisitor();
    }

    @Test
    public void tokenizePlainHtmlTag() {
        Node testNode = parser.parse("*text*");
        testNode.accept(htmlTokenVisitor);
        assertThat(htmlTokenVisitor.getTokens()).containsExactly(
                new Token("p", ImmutableMap.of(), true, false),
                new Token("em", ImmutableMap.of(), true, false),
                new Token("text", ImmutableMap.of(), false, false),
                new Token("em", ImmutableMap.of(), true, true),
                new Token("p", ImmutableMap.of(), true, true)
        );
    }

    @Test
    public void tokenizeHtmlTagWithProperties() {
        Node testNode = parser.parse("![text](/url.png)");
        testNode.accept(htmlTokenVisitor);
        assertThat(htmlTokenVisitor.getTokens()).containsExactly(
                new Token("p", ImmutableMap.of(), true, false),
                new Token("img", ImmutableMap.of("src", "/url.png",
                        "alt", "text"), true, true),
                new Token("p", ImmutableMap.of(), true, true));
    }

    @Test
    public void tokenizeNestedTags() {
        Node testNode = parser.parse(
                "1. First ordered list item\n");
        testNode.accept(htmlTokenVisitor);
        assertThat(htmlTokenVisitor.getTokens()).containsExactly(
                new Token("ol", ImmutableMap.of(), true, false),
                new Token("li", ImmutableMap.of(), true, false),
                new Token("First ordered list item", ImmutableMap.of(), false, false),
                new Token("li", ImmutableMap.of(), true, true),
                new Token("ol", ImmutableMap.of(), true, true)
        );
    }
}