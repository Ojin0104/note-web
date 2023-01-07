package com.note.springsecuritynote.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtInfo {
    private String grancType;
    private String accessToken;
    private String refreshToken;

}
