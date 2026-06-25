package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.ContactMessage;
import com.example.virtual_teacher.repositories.contracts.ContactRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContactRepositoryImpl implements ContactRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public ContactRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(ContactMessage message) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(message);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<ContactMessage> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<ContactMessage> query = session.createQuery(
                    "from ContactMessage order by createdAt desc", ContactMessage.class);
            return query.list();
        }
    }

    @Override
    public ContactMessage getByPublicId(String publicId) {
        try (Session session = sessionFactory.openSession()) {
            Query<ContactMessage> query = session.createQuery(
                    "from ContactMessage where publicId = :publicId", ContactMessage.class);
            query.setParameter("publicId", publicId);
            ContactMessage result = query.uniqueResult();
            if (result == null) {
                throw new EntityNotFoundException("ContactMessage", "publicId", publicId);
            }
            return result;
        }
    }

    @Override
    public void markAsRead(String publicId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            ContactMessage message = getByPublicId(publicId);
            message.setRead(true);
            session.merge(message);
            session.getTransaction().commit();
        }
    }

    @Override
    public long countUnread() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery(
                    "select count(m) from ContactMessage m where m.isRead = false", Long.class);
            return query.uniqueResult();
        }
    }
}
