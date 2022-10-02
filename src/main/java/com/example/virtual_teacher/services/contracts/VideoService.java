package com.example.virtual_teacher.services.contracts;

import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.Video;

import java.util.List;

public interface VideoService {


    Video getById(int id);

    Video create(Video video);

    Video update(Video video, User authUser);

}
