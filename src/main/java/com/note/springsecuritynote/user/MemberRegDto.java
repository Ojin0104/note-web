package com.note.springsecuritynote.user;

import lombok.*;

import java.time.LocalDate;
/*
**회원가입정보
 */

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MemberRegDto {

    private String email;

    private String password;

    private String gender;

    private LocalDate birth;

    private String nickname;

    private int adminNo;




}
