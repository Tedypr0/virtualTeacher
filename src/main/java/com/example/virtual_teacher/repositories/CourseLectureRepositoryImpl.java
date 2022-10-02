package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.models.CourseLecture;

import com.example.virtual_teacher.repositories.contracts.CourseLectureRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CourseLectureRepositoryImpl implements CourseLectureRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CourseLectureRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void create(CourseLecture courseLecture) {
        try (Session session = sessionFactory.openSession()) {

            session.save(courseLecture);

        }
    }

    @Override
    public void update(CourseLecture courseLecture) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(courseLecture);
            session.beginTransaction().commit();
        }
    }


    @Override
    public boolean isAddedToCourse(int courseId, int lectureId) {
        try (Session session = sessionFactory.openSession()) {
            Query<CourseLecture> query = session.createQuery("from CourseLecture where courseId = :courseId " +
                    "and lectureId = :lectureId", CourseLecture.class);
            query.setParameter("courseId", courseId);
            query.setParameter("lectureId", lectureId);
            return query.getResultList().size() >= 1;
        }
    }

    @Override
    public void delete(int  id) {
        try (Session session = sessionFactory.openSession()) {
            CourseLecture courseLectureToDelete = session.get(CourseLecture.class, id);
            session.beginTransaction();
            session.delete(courseLectureToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public CourseLecture getByCourseAndLectureId(int courseId, int lectureId) {
        try (Session session = sessionFactory.openSession()) {
            Query<CourseLecture> query = session.createQuery("from CourseLecture where courseId = :courseId " +
                    "and lectureId = :lectureId", CourseLecture.class);
            query.setParameter("courseId", courseId);
            query.setParameter("lectureId", lectureId);
            return query.getResultList().get(0);
        }
    }


}
