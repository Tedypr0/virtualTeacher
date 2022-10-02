package com.example.virtual_teacher.services.contracts;

import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.Note;
import com.example.virtual_teacher.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NoteService {

    List<Note> getAll();

    Note getById(int id);

    Note create(Note note);

    Note update(Note note, User authUser);

    Note delete(User authUser, int id);

    List<Note> getByUserId(int userId);
}
