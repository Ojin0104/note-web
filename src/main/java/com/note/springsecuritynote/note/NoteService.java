package com.note.springsecuritynote.note;

import com.note.springsecuritynote.user.Member;
import com.note.springsecuritynote.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    /**
     * 노트 조회
     * 유저는 본인의 노트만 조회할 수 있다.
     * 어드민은 모든 노트를 조회할 수 있다.
     *
     * @param member 노트를 찾을 유저
     * @return 유저가 조회할 수 있는 모든 노트 List
     */
    @Transactional(readOnly = true)
    public List<Note> findByMember(Member member) {
        if (member == null) {
            throw new UserNotFoundException();
        }
        if (member.isAdmin()) {
            return noteRepository.findAll(Sort.by(Direction.DESC, "id"));
        }
        return noteRepository.findByMemberOrderByIdDesc(member);
    }

    /**
     * 노트 저장
     *
     * @param member    노트 저장하는 유저
     * @param title   제목
     * @param content 내용
     * @return 저장된 노트
     */
    public Note saveNote(Member member, String title, String content) {
        if (member == null) {
            throw new UserNotFoundException();
        }
        return noteRepository.save(new Note(title, content, member));
    }

    /**
     * 노트 삭제
     *
     * @param member   삭제하려는 노트의 유저
     * @param noteId 노트 ID
     */
    public void deleteNote(Member member, Long noteId) {
        if (member == null) {
            throw new UserNotFoundException();
        }
        Note note = noteRepository.findByIdAndMember(noteId, member);
        if (note != null) {
            noteRepository.delete(note);
        }
    }
}
