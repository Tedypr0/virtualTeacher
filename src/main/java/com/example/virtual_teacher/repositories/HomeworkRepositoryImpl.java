package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.Homework;
import com.example.virtual_teacher.repositories.contracts.HomeworkRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Table;
import java.util.List;

@Repository
public class HomeworkRepositoryImpl implements HomeworkRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public HomeworkRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Homework getHomeworkById(int homeworkId) {
        try(Session session = sessionFactory.openSession()){
            Homework homework = session.get(Homework.class,homeworkId);
            if(homework == null){
                throw new EntityNotFoundException("homework", homeworkId);
            }
            return homework;
        }
    }

    @Override
    public Homework getByPublicId(String publicId) {
        try (Session session = sessionFactory.openSession()) {
            return PublicIdQueryHelper.findByPublicId(session, Homework.class, publicId, "Homework",
                    homework -> !homework.isDeleted());
        }
    }

    @Override
    public List<Homework> getByUserId(int userId) {
        try(Session session = sessionFactory.openSession()){
            Query<Homework> query = session.createQuery("from Homework where user.id = :userId", Homework.class);
            query.setParameter("userId",userId);
            return query.getResultList();
        }
    }

    @Override
    public Homework getByUserIdAndLectureId(int userId, int lectureId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Homework> query = session.createQuery(
                    "from Homework where user.id = :userId and lecture.id = :lectureId and isDeleted = false",
                    Homework.class);
            query.setParameter("userId", userId);
            query.setParameter("lectureId", lectureId);
            List<Homework> results = query.getResultList();
            if (results.isEmpty()) {
                return null;
            }
            return results.get(0);
        }
    }

    @Override
    public List<Homework> getByTeacherId(int teacherId) {
        try(Session session = sessionFactory.openSession()){
            Query<Homework> query = session.createQuery("from Homework where lecture.teacher.id = :teacherId and grade=0", Homework.class);
            query.setParameter("teacherId",teacherId);
            return query.getResultList();
        }
    }

    @Override
    public void create(Homework homework) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(homework);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Homework homework) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(homework);
            session.getTransaction().commit();
        }
    }

    @Override
    public void softDelete(int id) {
        try(Session session = sessionFactory.openSession()){
            Homework homework = session.get(Homework.class,id);
            session.beginTransaction();
            session.merge(homework);
            session.getTransaction().commit();
        }
    }
}
