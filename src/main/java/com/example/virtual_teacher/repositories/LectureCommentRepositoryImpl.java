package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.LectureComment;
import com.example.virtual_teacher.repositories.contracts.LectureCommentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LectureCommentRepositoryImpl implements LectureCommentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public LectureCommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public List<LectureComment> getAll(int lectureId) {
        try (Session session = sessionFactory.openSession()) {
            Query<LectureComment> query = session.createQuery("from LectureComment l " +
                    "where l.isDeleted = false and lectureId=:lectureId", LectureComment.class);
            query.setParameter("lectureId", lectureId);
            return query.getResultList();
        }
    }

    @Override
    public LectureComment getById(int commentId) {
        try (Session session = sessionFactory.openSession()) {
            LectureComment lectureComment = session.get(LectureComment.class, commentId);
            if (lectureComment == null || lectureComment.isDeleted()) {
                throw new EntityNotFoundException("Comment", commentId);
            }
            return lectureComment;
        }
    }

    @Override
    public LectureComment create(LectureComment lectureComment) {
        try (Session session = sessionFactory.openSession()) {
            session.save(lectureComment);
        }
        return lectureComment;
    }

    @Override
    public LectureComment update(LectureComment lectureComment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(lectureComment);
            session.getTransaction().commit();
            return lectureComment;
        }
    }

    @Override
    public LectureComment delete(int commentId) {
        try (Session session = sessionFactory.openSession()) {
            LectureComment lectureComment = session.get(LectureComment.class, commentId);
            if (lectureComment == null || lectureComment.isDeleted()) {
                throw new EntityNotFoundException("Comment", commentId);
            }
            lectureComment.setDeleted(true);
            session.beginTransaction();
            session.update(lectureComment);
            session.getTransaction().commit();
            return lectureComment;
        }
    }

    @Override
    public LectureComment getByLectureId(int lectureId) {
        try (Session session = sessionFactory.openSession()) {
            LectureComment lectureComment = session.get(LectureComment.class, lectureId);
            if (lectureComment == null || lectureComment.isDeleted()) {
                throw new EntityNotFoundException("Lecture", lectureId, "comments");
            }
            return lectureComment;
        }
    }
}
