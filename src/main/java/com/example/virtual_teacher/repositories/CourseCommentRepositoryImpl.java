package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.CourseComment;
import com.example.virtual_teacher.repositories.contracts.CourseCommentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseCommentRepositoryImpl implements CourseCommentRepository {

    private final SessionFactory sessionFactory;


    @Autowired
    public CourseCommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<CourseComment> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<CourseComment> query = session.createQuery("from CourseComment c " +
                    "where c.isDeleted = false", CourseComment.class);
            return query.getResultList();
        }
    }

    @Override
    public CourseComment getById(int commentId) {
        try (Session session = sessionFactory.openSession()) {
            CourseComment courseComment = session.get(CourseComment.class, commentId);
            if (courseComment == null || courseComment.isDeleted()) {
                throw new EntityNotFoundException("Comment", commentId);
            }
            return courseComment;
        }
    }

    @Override
    public CourseComment create(CourseComment courseComment) {
        try (Session session = sessionFactory.openSession()) {
            session.save(courseComment);
        }
        return courseComment;
    }

    @Override
    public CourseComment update(CourseComment courseComment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(courseComment);
            session.getTransaction().commit();
            return courseComment;
        }
    }

    @Override
    public CourseComment delete(int commentId) {
        try (Session session = sessionFactory.openSession()) {
            CourseComment commentToDelete = getById(commentId);
            if (commentToDelete == null || commentToDelete.isDeleted()) {
                throw new EntityNotFoundException("Comment", commentId);
            }
            commentToDelete.setDeleted(true);
            session.beginTransaction();
            session.update(commentToDelete);
            session.getTransaction().commit();
            return commentToDelete;
        }
    }

    @Override
    public List<CourseComment> getByCourseId(int courseId) {
        try (Session session = sessionFactory.openSession()) {
            Query<CourseComment> query = session.createQuery("from CourseComment c " +
                    "where c.isDeleted = false and courseId=:courseId", CourseComment.class);
            query.setParameter("courseId", courseId);
            return query.getResultList();
        }
    }

    @Override
    public long getCourseCommentsCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> count = session.createQuery("SELECT count (u) from CourseComment u");
            return count.getSingleResult();
        }
    }

}
