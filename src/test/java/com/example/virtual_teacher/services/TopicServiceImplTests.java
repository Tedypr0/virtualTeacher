package com.example.virtual_teacher.services;

import com.example.virtual_teacher.Helpers;
import com.example.virtual_teacher.models.Topic;
import com.example.virtual_teacher.repositories.contracts.TopicRepository;
import com.example.virtual_teacher.repositories.contracts.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class TopicServiceImplTests {

    @Mock
    TopicRepository mockRepository;

    @InjectMocks
    TopicServiceImpl service;

    @Test
    void getAll_should_callRepository(){
        //Arrange
        Mockito.when(mockRepository.getAll()).thenReturn(new ArrayList<>());

        // Act
        service.getAll();

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    void getById_should_callRepository(){
        Topic mockTopic = Helpers.createMockTopic();
        //Arrange
        Mockito.when(mockRepository.getById(mockTopic.getId())).thenReturn(mockTopic);

        // Act
        service.getById(mockTopic.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(mockTopic.getId());
    }

    @Test
    void getTopicByName_should_callRepository(){
        Topic mockTopic = Helpers.createMockTopic();
        //Arrange
        Mockito.when(mockRepository.getByTopicName(mockTopic.getTopicName())).thenReturn(mockTopic);

        // Act
        service.getByTopicName(mockTopic.getTopicName());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getByTopicName(mockTopic.getTopicName());
    }
}
