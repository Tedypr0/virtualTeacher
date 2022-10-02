package com.example.virtual_teacher.services.mappers;

import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.LectureComment;
import com.example.virtual_teacher.models.Note;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.LectureCommentDto;
import com.example.virtual_teacher.models.dtos.NoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NoteMapper {

    @Autowired
    public NoteMapper() {
    }

    public Note dtoToObj(NoteDto noteDto, Lecture lecture, User authUser) {
        Note note = new Note();
        note.setNote(noteDto.getNote());
        note.setLecture(lecture);
        note.setUserId(authUser.getId());
        return note;
    }

    public NoteDto objToDto(Note note) {
        List<Note> notes = new ArrayList<>();
        notes.add(note);
        return listObjToDto(notes).get(0);
    }

    public List<NoteDto> listObjToDto(List<Note> notes){
        List<NoteDto> result = new ArrayList<>();
        for(Note n: notes) {
            NoteDto noteDto = new NoteDto();
            noteDto.setNote(n.getNote());
            result.add(noteDto);
        }
        return result;
    }

    public Note dtoToObjForUpdate(Note originalNote, NoteDto noteDto) {
        originalNote.setNote(noteDto.getNote());
        return originalNote;
    }
}
