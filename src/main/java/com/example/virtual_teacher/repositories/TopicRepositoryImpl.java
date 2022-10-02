package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.Topic;
import com.example.virtual_teacher.repositories.contracts.TopicRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TopicRepositoryImpl implements TopicRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TopicRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Topic> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Topic> query = session.createQuery("from Topic", Topic.class);
            return query.getResultList();
        }
    }

    @Override
    public Topic getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Topic topic = session.get(Topic.class, id);
            if (topic == null) {
                throw new EntityNotFoundException("Topic", id);
            }
            return topic;
        }
    }

    @Override
    public Topic getByTopicName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Topic> query = session.createQuery("from Topic where lower(topicName) = lower(:name)", Topic.class);
            query.setParameter("name", name);
            return query.getResultList().get(0);
        }
    }
}
