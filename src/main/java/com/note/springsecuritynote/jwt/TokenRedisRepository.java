package com.note.springsecuritynote.jwt;

import antlr.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface TokenRedisRepository extends CrudRepository<RefreshToken,Long> {


}

