package com.note.springsecuritynote.note;

import com.note.springsecuritynote.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByMemberOrderByIdDesc(Member member);

    Note findByIdAndMember(Long id, Member member);
}
