package com.note.springsecuritynote.jwt;

/**
 * JWT 기본 설정값
 */
public class JwtProperties {
    public static final int EXPIRATION_TIME = 60000; // 1분
    public static final String COOKIE_NAME = "JWT-AUTHENTICATION";
}