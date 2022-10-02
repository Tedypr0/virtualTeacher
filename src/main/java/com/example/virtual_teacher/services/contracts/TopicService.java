package com.example.virtual_teacher.services.contracts;

import com.example.virtual_teacher.models.Topic;

import java.util.List;

public interface TopicService {

    List<Topic> getAll();

    Topic getById(int id);

    Topic getByTopicName(String name);
}
