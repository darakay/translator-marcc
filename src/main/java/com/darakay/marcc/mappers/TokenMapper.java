package com.darakay.marcc.mappers;

import com.darakay.marcc.domain.Token;
import com.darakay.marcc.dto.TokenDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenMapper {

    TokenDto tokenToTokenDto(Token token);
}
