package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.FilterOptionsCourses;
import com.example.virtual_teacher.models.UsersCourses;
import com.example.virtual_teacher.repositories.contracts.CourseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseRepositoryImpl implements CourseRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CourseRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Course> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createQuery("from Course where isDeleted = false and isDraft = false", Course.class);

            return query.getResultList();
        }
    }

    @Override
    public List<UsersCourses> getByUserId(int studentId) {
        try (Session session = sessionFactory.openSession()) {
            Query<UsersCourses> query = session.createQuery("from UsersCourses where userId = :studentId", UsersCourses.class);
            query.setParameter("studentId", studentId);
            return query.getResultList();
        }
    }

    @Override
    public List<Course> getByTeacherId(int teacherId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createQuery("from Course where isDeleted = false and teacher.id = :teacherId", Course.class);
            query.setParameter("teacherId", teacherId);
            return query.getResultList();
        }
    }

    @Override
    public Course getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Course course = session.get(Course.class, id);
            if (course == null || course.isDeleted()) {
                throw new EntityNotFoundException("Course", id);
            }
            return course;
        }
    }

    @Override
    public long courseCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> count = session.createQuery("SELECT count (u) from Course u");
            return count.getSingleResult();
        }
    }

    @Override
    public Course getByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createQuery("from Course where title = :title", Course.class);
            query.setParameter("title", title);
            List<Course> courses = query.getResultList();
            if (courses.size() == 0) {
                throw new EntityNotFoundException("Course", "title", title);
            }
            return courses.get(0);
        }
    }

    @Override
    public Course create(Course course) {
        try (Session session = sessionFactory.openSession()) {
            session.save(course);
        }
        return course;
    }

    @Override
    public Course update(Course course) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(course);
            session.getTransaction().commit();
            return course;
        }
    }

    @Override
    public Course delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            Course courseToDelete = session.get(Course.class, id);
            if (courseToDelete == null) {
                throw new EntityNotFoundException("Course", id);
            }
            courseToDelete.setDeleted(true);
            session.beginTransaction();
            session.update(courseToDelete);
            session.getTransaction().commit();
            return courseToDelete;
        }
    }

    @Override
    public List<Course> filter(FilterOptionsCourses filterOptionsCourses) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder("from Course where 1=1 and isDeleted = false and isDraft = false");

            if (filterOptionsCourses.getTitle().isPresent()) {
                queryString.append(" and title like :title");
            }
            if (filterOptionsCourses.getTopicId().isPresent()) {
                queryString.append(" and topic_id like :topicId");
            }

            if (filterOptionsCourses.getTeacherId().isPresent()) {
                queryString.append(" and user_id like :teacherId");
            }

            if (filterOptionsCourses.getRating().isPresent()) {
                queryString.append(" and avg_rating like :avgRating");
            }

            addSorting(filterOptionsCourses, queryString);

            Query<Course> query = session.createQuery(queryString.toString(), Course.class);

            if (filterOptionsCourses.getTitle().isPresent()) {
                query.setParameter("title", String.format("%%%s%%", filterOptionsCourses.getTitle().get()));
            }
            if (filterOptionsCourses.getTopicId().isPresent()) {
                query.setParameter("topicId", filterOptionsCourses.getTopicId().get());
            }
            if (filterOptionsCourses.getTeacherId().isPresent()) {
                query.setParameter("teacherId", filterOptionsCourses.getTeacherId().get());
            }
            if (filterOptionsCourses.getRating().isPresent()) {
                query.setParameter("avgRating", filterOptionsCourses.getRating().get());
            }
            return query.list();
        }
    }

    private void addSorting(FilterOptionsCourses filterOptionsCourses, StringBuilder queryString) {
        if (filterOptionsCourses.getSortBy().isEmpty()) {
            return;
        }

        String orderBy = "";
        switch (filterOptionsCourses.getSortBy().get()) {
            case "title":
                orderBy = "title";
                break;
            case "topicId":
                orderBy = "topicId";
                break;
            case "teacherId":
                orderBy = "teacherId";
                break;
            case "rating":
                orderBy = "avgRating";
                break;
            default:
                return;
        }
        queryString.append(String.format(" order by %s", orderBy));

        if (filterOptionsCourses.getSortOrder().isPresent() && filterOptionsCourses.getSortOrder().get().equalsIgnoreCase("desc")) {
            queryString.append(" desc");
        }
    }
}
