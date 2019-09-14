package com.darakay.marcc.config;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.Renderer;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TranslatorConfiguration {

    @Bean
    public Parser parser(){
        return Parser.builder().build();
    }

    @Bean
    public Renderer htmlRenderer(){
        return HtmlRenderer.builder().build();
    }
}
