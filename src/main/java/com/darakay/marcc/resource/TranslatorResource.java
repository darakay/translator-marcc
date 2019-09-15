package com.darakay.marcc.resource;

import com.darakay.marcc.domain.Token;
import com.darakay.marcc.dto.TokenDto;
import com.darakay.marcc.dto.TranslateRequestDto;
import com.darakay.marcc.mappers.TokenMapper;
import com.darakay.marcc.services.TranslatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/translator")
public class TranslatorResource {

    private final TranslatorService translatorService;
    private final TokenMapper dtoMapper;

    public TranslatorResource(TranslatorService translatorService, TokenMapper dtoMapper) {
        this.translatorService = translatorService;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping("/translate-task")
    public ResponseEntity<List<TokenDto>> translate(@RequestBody TranslateRequestDto translateRequestDto){
        List<Token> tokens = translatorService.translate(translateRequestDto.getValue());

        return ResponseEntity.ok(tokens.stream().map(dtoMapper::tokenToTokenDto).collect(Collectors.toList()));
    }
}
