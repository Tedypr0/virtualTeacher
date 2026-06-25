package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.ContactMessage;
import com.example.virtual_teacher.repositories.contracts.ContactRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public void markAsRead(String publicId, int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery(
                    "INSERT IGNORE INTO contact_message_reads (contact_message_id, user_id) " +
                    "SELECT cm.contact_message_id, :uid FROM contact_messages cm " +
                    "WHERE cm.public_id = :publicId",
                    Void.class)
                    .setParameter("uid", userId)
                    .setParameter("publicId", publicId)
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public long countUnread(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Number result = (Number) session.createNativeQuery(
                    "SELECT COUNT(*) FROM contact_messages cm " +
                    "WHERE cm.contact_message_id NOT IN (" +
                    "  SELECT cmr.contact_message_id FROM contact_message_reads cmr WHERE cmr.user_id = :uid" +
                    ")")
                    .setParameter("uid", userId)
                    .uniqueResult();
            return result != null ? result.longValue() : 0L;
        }
    }

    @Override
    public Set<String> getReadPublicIds(int userId) {
        try (Session session = sessionFactory.openSession()) {
            List<String> ids = session.createNativeQuery(
                    "SELECT cm.public_id FROM contact_messages cm " +
                    "JOIN contact_message_reads cmr ON cm.contact_message_id = cmr.contact_message_id " +
                    "WHERE cmr.user_id = :uid",
                    String.class)
                    .setParameter("uid", userId)
                    .list();
            return new HashSet<>(ids);
        }
    }
}
