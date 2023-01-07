package com.note.springsecuritynote.note;

import com.note.springsecuritynote.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;

    /**
     * 노트(게시글) 조회
     *
     * @return 노트 view (note/index.html)
     */
    @GetMapping
    public String getNote(Authentication authentication, Model model) {
        Member member = (Member) authentication.getPrincipal();
        List<Note> notes = noteService.findByMember(member);
        // note/index.html 에서 notes 사용가능
        model.addAttribute("notes", notes);//(notes)에 notes값 넣어줌
        // note/index.html 제공
        return "note/index";
    }

    /**
     * 노트 저장
     */
    @PostMapping
    public String saveNote(Authentication authentication, @ModelAttribute NoteRegisterDto noteDto) {
        Member member = (Member) authentication.getPrincipal();
        noteService.saveNote(member, noteDto.getTitle(), noteDto.getContent());
        return "redirect:note";
    }

    /**
     * 노트 삭제
     */
    @DeleteMapping
    public String deleteNote(Authentication authentication, @RequestParam Long id) {
        Member member = (Member) authentication.getPrincipal();
        noteService.deleteNote(member, id);
        return "redirect:note";
    }
}

