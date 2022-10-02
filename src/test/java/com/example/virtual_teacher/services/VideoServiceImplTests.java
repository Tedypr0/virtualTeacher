package com.example.virtual_teacher.services;

import com.example.virtual_teacher.Helpers;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.UserRole;
import com.example.virtual_teacher.models.Video;
import com.example.virtual_teacher.repositories.contracts.VideoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VideoServiceImplTests {
    
    @Mock
    VideoRepository mockRepository;
    
    @InjectMocks
    VideoServiceImpl service;
    
    
    @Test
    void getById_should_callRepository(){
        //Arrange
        Video mockVideo = Helpers.createMockVideo();
        Mockito.when(mockRepository.getById(mockVideo.getVideoId())).thenReturn(mockVideo);

        // Act
        service.getById(mockVideo.getVideoId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(mockVideo.getVideoId());
    }

    @Test
    void create_should_callRepository(){
        //Arrange
        Video mockVideo = Helpers.createMockVideo();
        Mockito.when(mockRepository.create(mockVideo)).thenReturn(mockVideo);

        // Act
        service.create(mockVideo);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(mockVideo);
    }

    @Test
    void update_should_callRepository_whenInitiatorIsTeacher(){
        //Arrange
        Video mockVideo = Helpers.createMockVideo();
        User initiator = Helpers.createMockTeacher();
        Mockito.when(mockRepository.update(mockVideo)).thenReturn(mockVideo);

        // Act
        service.update(mockVideo ,initiator);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockVideo);
    }

}
