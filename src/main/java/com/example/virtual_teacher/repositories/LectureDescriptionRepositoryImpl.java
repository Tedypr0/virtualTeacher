package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.LectureDescription;
import com.example.virtual_teacher.repositories.contracts.LectureDescriptionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LectureDescriptionRepositoryImpl implements LectureDescriptionRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public LectureDescriptionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public List<LectureDescription> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<LectureDescription> query = session.createQuery("from LectureDescription l "
                    + "where l.isDeleted = false", LectureDescription.class);
            return query.getResultList();
        }
    }

    @Override
    public LectureDescription getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            LectureDescription lectureDescription = session.get(LectureDescription.class, id);
            if (lectureDescription == null || lectureDescription.isDeleted()) {
                throw new EntityNotFoundException("Lecture", id);
            }
            return lectureDescription;
        }
    }

    @Override
    public String getByLectureId(int lectureId) {
        try (Session session = sessionFactory.openSession()) {
            Query<LectureDescription> query = session.createQuery(
                    "from LectureDescription l where l.lectureId = :id and isDeleted = false",
                    LectureDescription.class);
            query.setParameter("id", lectureId);
            if (query.getResultList().size() == 0) {
                return "No description";
            }
                return query.getResultList().get(0).toString();
        }
    }

    @Override
    public LectureDescription create(LectureDescription lectureDescription) {
        try (Session session = sessionFactory.openSession()) {
          session.beginTransaction();
            session.save(lectureDescription);
            session.getTransaction().commit();
        }
        return lectureDescription;
    }

    @Override
    public LectureDescription update(LectureDescription lectureDescription) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(lectureDescription);
            session.getTransaction().commit();
            return lectureDescription;
        }
    }

    @Override
    public LectureDescription delete(LectureDescription lectureDescription) {
        try (Session session = sessionFactory.openSession()) {
            if (lectureDescription.isDeleted()) {
                throw new EntityNotFoundException("Lecture Description", lectureDescription.getId());
            }
            lectureDescription.setDeleted(true);
            session.beginTransaction();
            session.update(lectureDescription);
            session.getTransaction().commit();
            return lectureDescription;
        }
    }

//    @Override
//    public LectureDescription getByLectureId(int lectureId) {
//        try (Session session = sessionFactory.openSession()) {
//            LectureDescription lectureDescription = session.get(LectureDescription.class, lectureId);
//            if (lectureDescription == null || lectureDescription.isDeleted()) {
//                throw new EntityNotFoundException("Lecture", lectureId, "description");
//            }
//            return lectureDescription;
//        }
//    }
}
