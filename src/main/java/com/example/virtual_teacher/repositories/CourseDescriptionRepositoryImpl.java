package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.CourseDescription;
import com.example.virtual_teacher.models.LectureDescription;
import com.example.virtual_teacher.repositories.contracts.CourseDescriptionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDescriptionRepositoryImpl implements CourseDescriptionRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CourseDescriptionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<CourseDescription> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<CourseDescription> query = session.createQuery("from CourseDescription c "
                    + "where c.isDeleted = false", CourseDescription.class);
            return query.getResultList();
        }
    }

    @Override
    public String getByCourseId(int courseId) {
        try (Session session = sessionFactory.openSession()) {
            Query<CourseDescription> query = session.createQuery(
                    "from CourseDescription c where c.courseId = :id and isDeleted = false",
                    CourseDescription.class);
            query.setParameter("id", courseId);
            if (query.getResultList().size() == 0) {
                return "No description";
            }
            return query.getResultList().get(0).toString();
        }
    }

    @Override
    public CourseDescription create(CourseDescription courseDescription) {
        try (Session session = sessionFactory.openSession()){
            session.save(courseDescription);
        }
        return courseDescription;
    }

    @Override
    public CourseDescription update(CourseDescription courseDescription) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(courseDescription);
            session.getTransaction().commit();
            return courseDescription;
        }
    }

    @Override
    public CourseDescription delete(int descriptionId) {
        try (Session session = sessionFactory.openSession()) {
            CourseDescription courseDescription = session.get(CourseDescription.class, descriptionId);
            if (courseDescription == null || courseDescription.isDeleted()) {
                throw new EntityNotFoundException("Course Description", descriptionId);
            }
            courseDescription.setDeleted(true);
            session.beginTransaction();
            session.update(courseDescription);
            session.getTransaction().commit();
            return courseDescription;
        }
    }
}
