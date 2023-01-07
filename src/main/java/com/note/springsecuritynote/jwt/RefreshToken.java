package com.note.springsecuritynote.jwt;

import ch.qos.logback.core.boolex.EvaluationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@AllArgsConstructor
@RedisHash(value="refreshToken",timeToLive=60)
public class RefreshToken {

    @Id
    private Long userId;

    private String token;
}
