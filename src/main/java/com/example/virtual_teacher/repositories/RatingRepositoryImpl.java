package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.Rating;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.repositories.contracts.CourseRepository;
import com.example.virtual_teacher.repositories.contracts.RatingRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Repository
public class RatingRepositoryImpl implements RatingRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public RatingRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Rating> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Rating> query = session.createQuery("from Rating", Rating.class);
            return query.getResultList();
        }
    }

    @Override
    public Rating getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Rating rating = session.get(Rating.class, id);
            if (rating == null) {
                throw new EntityNotFoundException("Rating", id);
            }
            return rating;
        }
    }

    @Override
    public List<Rating> getByCourseId(int courseId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Rating> query = session.createNativeQuery(
                    "SELECT r.* FROM ratings r WHERE r.course_id = :courseId",
                    Rating.class);
            query.setParameter("courseId", courseId);
            return query.list();
        }
    }

    @Override
    public Rating create(User user, Rating rating) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(rating);
            session.getTransaction().commit();
        }
        return rating;
    }

    @Override
    public Rating update(User user, Rating rating) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(rating);
            session.getTransaction().commit();
            return rating;
        }
    }

    @Override
    public Rating delete(User user, int courseId) {
        Rating ratingToDelete = getById(courseId);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(ratingToDelete);
            session.getTransaction().commit();
            return ratingToDelete;
        }
    }

    @Override
    public double getAvgRatingByCourseId(int courseId) {
        OptionalDouble avgRating = getByCourseId(courseId).stream().mapToDouble(Rating::getRatingScore).average();
        if (avgRating.isPresent()) {
            return avgRating.getAsDouble();
        }
        return 0;
    }

    @Override
    public long getRatingsCount(int courseId) {
        return getByCourseId(courseId).size();
    }

}

