package com.darakay.marcc.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect
@EqualsAndHashCode
public class TokenDto {
    private String value;
    private boolean isTerm;
}
