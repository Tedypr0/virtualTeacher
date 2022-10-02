package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.Video;
import com.example.virtual_teacher.repositories.contracts.VideoRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VideoRepositoryImpl implements VideoRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public VideoRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Video getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Video video = session.get(Video.class, id);
            if (video == null || video.isDeleted()) {
                throw new EntityNotFoundException("Video", id);
            }
            return video;
        }
    }

    @Override
    public Video create(Video video) {
        try (Session session = sessionFactory.openSession()) {
            session.save(video);
        }
        return video;
    }

    @Override
    public Video update(Video video) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.update(video);
            session.getTransaction().commit();
        }
        return video;
    }

    @Override
    public Video delete(int id) {
        try(Session session = sessionFactory.openSession()){
            Video video = session.get(Video.class, id);
            if(video == null || video.isDeleted()){
                throw new EntityNotFoundException("Video",id);
            }
            video.setDeleted(true);
            session.beginTransaction();
            session.update(video);
            session.getTransaction().commit();
            return video;
        }
    }
}
