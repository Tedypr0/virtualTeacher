package com.example.virtual_teacher.services.mappers;

import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.Video;
import com.example.virtual_teacher.models.dtos.LectureDto;
import com.example.virtual_teacher.models.dtos.NewLectureDto;
import com.example.virtual_teacher.models.dtos.UpdateLectureDto;
import com.example.virtual_teacher.repositories.contracts.LectureDescriptionRepository;
import com.example.virtual_teacher.services.contracts.LectureDescriptionService;
import com.example.virtual_teacher.services.contracts.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LectureMapper {

    @Autowired
    public LectureMapper() {
    }

    public static Lecture dtoToObject(LectureDto lectureDto) {
        Lecture lecture = new Lecture();
            lecture.setTitle(lectureDto.getTitle());
            lecture.setDescription(lectureDto.getDescription());
            lecture.setTeacher(lectureDto.getTeacher());
            lecture.getVideo().setVideoUrl(lectureDto.getVideo().getVideoUrl());
        return lecture;
    }

    public static Lecture dtoToObject(LectureDto lectureDto, Lecture originalLecture) {
        originalLecture.setTitle(lectureDto.getTitle());
        originalLecture.setDescription(lectureDto.getDescription());
        originalLecture.setTeacher(lectureDto.getTeacher());
        originalLecture.getVideo().setVideoUrl(lectureDto.getVideo().getVideoUrl());
        return originalLecture;
    }

    public static LectureDto objToDto(Lecture lecture) {
        LectureDto lectureDto = new LectureDto();
        lectureDto.setTitle(lecture.getTitle());
        lectureDto.setDescription(lecture.getDescription());
        lectureDto.setTeacher(lecture.getTeacher());
        lectureDto.getVideo().setVideoUrl(lecture.getVideo().getVideoUrl());
        return lectureDto;
    }

    public static List<LectureDto> listObjToDto(List<Lecture> lectures){
        List<LectureDto> result = new ArrayList<>();
        for(Lecture l: lectures) {
            LectureDto lectureDto = new LectureDto();
            lectureDto.setTitle(l.getTitle());
            lectureDto.setDescription(l.getDescription());
            lectureDto.setTeacher(l.getTeacher());
            lectureDto.getVideo().setVideoUrl(l.getVideo().getVideoUrl());
            result.add(lectureDto);
        }
        return result;
    }

    public Lecture newLectureDtoToObj(NewLectureDto newLectureDto, User authUser, Video video) {
        Lecture lecture = new Lecture();
        lecture.setTitle(newLectureDto.getTitle());
        lecture.setTeacher(authUser);
        lecture.setVideo(video);
        return lecture;
    }

    public UpdateLectureDto updateLectureObjToDto(Lecture lecture) {
       UpdateLectureDto updateLectureDto = new UpdateLectureDto();
        updateLectureDto.setTitle(lecture.getTitle());
        updateLectureDto.setDescription(lecture.getDescription().getDescription());
        updateLectureDto.setVideoUrl(lecture.getVideo().getVideoUrl());
        return updateLectureDto;
    }

    public Lecture updateLectureDtoToObj(Lecture originalLecture, UpdateLectureDto updateLectureDto) {
        originalLecture.setTitle(updateLectureDto.getTitle());
        if (updateLectureDto.getDescription() != null && !updateLectureDto.getDescription().equals(
        "No Description")) {
            originalLecture.getDescription().setDescription(updateLectureDto.getDescription());
            originalLecture.getDescription().setLectureId(originalLecture.getId());
        }
        //originalLecture.getVideo().setVideoUrl(updateLectureDto.getVideoUrl());
        return originalLecture;
    }
}
