package com.darakay.marcc.resource;

import com.darakay.marcc.dto.TokenDto;
import com.darakay.marcc.dto.TranslateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TranslatorResourceTest {

    private static final String URL = "/v1/marcc/translate-task";

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void correctTranslateText() throws Exception {
        TranslateRequestDto requestDto = TranslateRequestDto.builder().value("*Hello, world!*").build();

        List<TokenDto> expectedTokens = ImmutableList.of(
                TokenDto.builder().value("<p").isTerm(true).build(),
                TokenDto.builder().value("<em>").isTerm(true).build(),
                TokenDto.builder().value("Hello, world!").isTerm(false).build(),
                TokenDto.builder().value("</em>").isTerm(true).build(),
                TokenDto.builder().value("</p>").isTerm(true).build());

        mockMvc.perform(
                post(URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(mapper.writeValueAsString(expectedTokens)));
    }
}