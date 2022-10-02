package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.repositories.contracts.LectureRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LectureRepositoryImpl implements LectureRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public LectureRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Lecture> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Lecture> query = session.createQuery("from Lecture where isDeleted = false", Lecture.class);
            return query.getResultList();
        }
    }

    @Override
    public Lecture getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Lecture lecture = session.get(Lecture.class, id);
            if (lecture == null || lecture.isDeleted()) {
                throw new EntityNotFoundException("Lecture", id);
            }
            return lecture;
        }
    }

    @Override
    public List<Lecture> lecturesByCourseId(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Lecture> query = session.createQuery("from Lecture l where l.course.id = :id", Lecture.class);
                    query.setParameter("id", id);
            return query.getResultList();
        }
    }


    @Override
    public Lecture getByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Lecture> query = session.createQuery("from Lecture where lower(title) = lower(:title)", Lecture.class);
            query.setParameter("title", title);
            List<Lecture> lectures = query.getResultList();
            if (lectures.isEmpty()) {
                throw new EntityNotFoundException("Lecture", "title", title);
            }
            return lectures.get(0);
        }
    }

    @Override
    public long lectureCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> count = session.createQuery("SELECT count (u) from Lecture u");
            return count.getSingleResult();
        }
    }

    @Override
    public Lecture create(Lecture lecture) {
        try (Session session = sessionFactory.openSession()) {
            session.save(lecture);
        }
        return lecture;
    }

    @Override
    public Lecture update(Lecture lecture) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(lecture);
            session.getTransaction().commit();
            return lecture;
        }
    }

    @Override
    public Lecture delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            Lecture lecture = session.get(Lecture.class, id);
            if (lecture == null) {
                throw new EntityNotFoundException("Lecture", id);
            }
            lecture.setDeleted(true);
            session.beginTransaction();
            session.update(lecture);
            session.getTransaction().commit();
            return lecture;
        }
    }

    @Override
    public List<Lecture> getAllByTeacherId(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Lecture> query = session.createQuery("select l from Lecture l where isDeleted = false and l.teacher.id = :id")
                    .setParameter("id", id);
            return query.getResultList();
        }
    }


}
