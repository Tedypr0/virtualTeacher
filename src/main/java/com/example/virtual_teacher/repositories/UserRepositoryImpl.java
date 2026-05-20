package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.MotivationalLetter;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.UsersCourses;
import com.example.virtual_teacher.repositories.contracts.UserRepository;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            return query.getResultList();
        }
    }

    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public User getByIdForSession(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            Hibernate.initialize(user.getRole());
            return user;
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);
            List<User> users = query.getResultList();
            if (users.size() == 0) {
                throw new EntityNotFoundException("User", "email", email);
            }
            return users.get(0);
        }
    }

    @Override
    public User create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.persist(user);
            return user;
        }
    }

    @Override
    public User update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
            return user;
        }
    }

    @Override
    public User delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
            return user;
        }
    }

    @Override
    public boolean applicationExists(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<MotivationalLetter> query = session.createQuery("from MotivationalLetter where userId = :userId", MotivationalLetter.class);
            query.setParameter("userId", userId);
            return query.getResultList().size() >= 1;
        }
    }

    @Override
    public void createTeacherApplication(int userId, MotivationalLetter motivationalLetter) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(motivationalLetter);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteTeacherApplication(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<MotivationalLetter> query = session.createQuery(
                    "from MotivationalLetter where userId = :userId", MotivationalLetter.class);
            query.setParameter("userId", userId);
            MotivationalLetter motivationalLetter = query.uniqueResult();
            if (motivationalLetter == null) {
                return;
            }
            session.beginTransaction();
            session.remove(motivationalLetter);
            session.getTransaction().commit();
        }
    }

    @Override
    public boolean isEnrolled(int courseId, int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<UsersCourses> query = session.createQuery("from UsersCourses where course.id = :courseId and userId = :userId", UsersCourses.class);
            query.setParameter("courseId", courseId);
            query.setParameter("userId", userId);
            return query.getResultList().size() >= 1;
        }
    }

    @Override
    public void enrollToCourse(UsersCourses usersCourses) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(usersCourses);
            session.getTransaction().commit();
        }
    }

    @Override
    public long getAllTeachers() {
        try(Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("from User u where u.role.id = 2", User.class);

            return query.getResultList().size();
        }
    }

    @Override
    public long getAllStudents() {
        try(Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("from User u where u.role.id = 1 ", User.class);
            return query.getResultList().size();
        }
    }

    @Override
    public List<User> getAllTeacherApplications() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(
                    "from User u where exists (select 1 from MotivationalLetter m where m.userId = u.id)",
                    User.class);
            return query.getResultList();
        }
    }

    @Override
    public void updateProfileImage(int userId, byte[] data, String contentType) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, userId);
            if (user == null) {
                throw new EntityNotFoundException("User", userId);
            }
            session.beginTransaction();
            user.setProfileImage(data);
            user.setProfileImageContentType(contentType);
            user.setProfileImageUpdatedAt(LocalDateTime.now());
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public User getByIdWithProfileImage(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            Hibernate.initialize(user.getProfileImage());
            return user;
        }
    }
}
