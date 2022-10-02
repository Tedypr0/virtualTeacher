package com.example.virtual_teacher.services;

import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.Video;
import com.example.virtual_teacher.repositories.contracts.VideoRepository;
import com.example.virtual_teacher.services.contracts.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;

    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public Video getById(int id) {
        return videoRepository.getById(id);
    }

    @Override
    public Video create(Video video) {
        return videoRepository.create(video);
    }

    @Override
    public Video update(Video video, User authUser) {
        return videoRepository.update(video);
    }

}
