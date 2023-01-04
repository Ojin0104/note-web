package com.note.springsecuritynote.notice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;


import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class NoticeControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext webApplicationContext){
        mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }
    @AfterEach
    void tearDown() {
    }

    @Test
    void getNotice_인증없음() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/notice"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

    }

    @Test
    @WithMockUser//가짜유저등록
    void getNotice_인증있음() throws Exception {
        mockMvc.perform(get("/notice"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles={"ADMIN"},username="admin",password="admin")
    void postNotice() throws Exception {
        mockMvc.perform(
                post("/notice")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title","제목")
                .param("content","내용")
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("notice"));
    }

    @Test
    void deleteNotice() {
    }
}