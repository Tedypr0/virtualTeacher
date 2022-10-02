package com.example.virtual_teacher.repositories.contracts;

import com.example.virtual_teacher.models.Note;
import com.example.virtual_teacher.models.Video;

import java.util.List;

public interface VideoRepository {

    Video getById(int id);

    Video create(Video video);

    Video update(Video video);

    Video delete(int Id);
}
