package com.example.virtual_teacher.repositories.contracts;

import com.example.virtual_teacher.models.Note;

import java.util.List;

public interface NoteRepository {

    List<Note> getAll();

    Note getById(int descriptionId);

    Note create(Note note);

    Note update(Note note);

    Note delete(int descriptionId);

    List<Note> getByLectureId(int lectureId);

    List<Note> getByUserId(int userId);
}
