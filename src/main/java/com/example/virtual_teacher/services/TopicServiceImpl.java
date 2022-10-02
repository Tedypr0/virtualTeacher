package com.example.virtual_teacher.services;

import com.example.virtual_teacher.models.Topic;
import com.example.virtual_teacher.repositories.contracts.TopicRepository;
import com.example.virtual_teacher.services.contracts.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    @Autowired
    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public List<Topic> getAll() {
        return topicRepository.getAll();
    }

    @Override
    public Topic getById(int id) {
        return topicRepository.getById(id);
    }

    @Override
    public Topic getByTopicName(String name) {
        return topicRepository.getByTopicName(name);
    }
}
