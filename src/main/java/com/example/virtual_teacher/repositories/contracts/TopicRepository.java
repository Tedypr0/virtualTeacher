package com.example.virtual_teacher.repositories.contracts;

import com.example.virtual_teacher.models.Topic;

import java.util.List;

public interface TopicRepository {
    List<Topic> getAll();

    Topic getById(int id);

    Topic getByTopicName(String name);
}
