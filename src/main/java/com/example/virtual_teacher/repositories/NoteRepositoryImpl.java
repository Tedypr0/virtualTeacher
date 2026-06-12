package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.Note;
import com.example.virtual_teacher.repositories.contracts.NoteRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoteRepositoryImpl implements NoteRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public NoteRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public List<Note> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Note> query = session.createQuery("from Note n " +
                    "where n.isDeleted = false", Note.class);
            return query.getResultList();
        }
    }

    @Override
    public Note getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Note note = session.get(Note.class, id);
            if (note == null || note.isDeleted()) {
                throw new EntityNotFoundException("Note", id);
            }
            return note;
        }
    }

    @Override
    public Note getByPublicId(String publicId) {
        try (Session session = sessionFactory.openSession()) {
            return PublicIdQueryHelper.findByPublicId(session, Note.class, publicId, "Note",
                    note -> !note.isDeleted());
        }
    }

    @Override
    public Note create(Note note) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(note);
            session.getTransaction().commit();
        }
        return note;
    }

    @Override
    public Note update(Note note) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(note);
            session.getTransaction().commit();
            return note;
        }
    }

    @Override
    public Note delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            Note note = session.get(Note.class, id);
            if (note == null) {
                throw new EntityNotFoundException("Note", id);
            }
            note.setDeleted(true);
            session.beginTransaction();
            session.merge(note);
            session.getTransaction().commit();
            return note;
        }
    }

    @Override
    public List<Note> getByLectureId(int lectureId) {
        try(Session session = sessionFactory.openSession()){
            Query<Note> query = session.createQuery("from Note where " +
                    "lecture.id = :lectureId and isDeleted = false",Note.class);
            query.setParameter("lectureId",lectureId);
            return query.getResultList();
        }
    }

    @Override
    public List<Note> getByUserId(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Note> query = session.createQuery(
                    "select n from Note n join fetch n.lecture l left join fetch l.course " +
                            "where n.userId = :userId and n.isDeleted = false",
                    Note.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        }
    }

    @Override
    public Note getByUserIdAndLectureId(int userId, int lectureId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Note> query = session.createQuery(
                    "from Note n where n.userId = :userId and n.lecture.id = :lectureId and n.isDeleted = false",
                    Note.class);
            query.setParameter("userId", userId);
            query.setParameter("lectureId", lectureId);
            List<Note> results = query.getResultList();
            if (results.isEmpty()) {
                return null;
            }
            return results.get(0);
        }
    }
}
