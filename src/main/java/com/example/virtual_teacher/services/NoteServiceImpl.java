package com.example.virtual_teacher.services;

import com.example.virtual_teacher.models.Note;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.repositories.contracts.NoteRepository;
import com.example.virtual_teacher.services.contracts.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public List<Note> getAll() {
        return noteRepository.getAll();
    }

    @Override
    public Note getById(int id) {
        return noteRepository.getById(id);
    }

    @Override
    public Note create(Note note) {
        return noteRepository.create(note);
    }

    @Override
    public Note update(Note note, User authUser) {
        return noteRepository.update(note);
    }

    @Override
    public Note delete(User authUser, int id) {
        return noteRepository.delete(id);
    }

    @Override
    public List<Note> getByUserId(int userId) {return noteRepository.getByUserId(userId);}
}
