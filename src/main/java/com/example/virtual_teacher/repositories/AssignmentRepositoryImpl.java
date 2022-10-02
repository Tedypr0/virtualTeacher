package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.Assignment;
import com.example.virtual_teacher.repositories.contracts.AssignmentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AssignmentRepositoryImpl implements AssignmentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public AssignmentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Assignment getByAssignmentByLectureId(int assignmentId) {
        try (Session session = sessionFactory.openSession()) {
            Assignment assignment = session.get(Assignment.class, assignmentId);
            if (assignment == null) {
                throw new EntityNotFoundException("Assignment", assignmentId);
            }
            return assignment;
        }
    }

    @Override
    public Assignment create(Assignment assignment) {
        try (Session session = sessionFactory.openSession()) {
            session.save(assignment);
        }
        return assignment;
    }

    @Override
    public Assignment update(Assignment assignment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(assignment);
            session.getTransaction().commit();
            return assignment;
        }
    }

    @Override
    public Assignment delete(int assignmentId) {
        try (Session session = sessionFactory.openSession()) {
            Assignment assignment = session.get(Assignment.class, assignmentId);
            if (assignment == null) {
                throw new EntityNotFoundException("Assignment", assignmentId);
            }
            assignment.setDeleted(true);
            session.beginTransaction();
            session.update(assignment);
            session.getTransaction().commit();
            return assignment;
        }
    }
}
