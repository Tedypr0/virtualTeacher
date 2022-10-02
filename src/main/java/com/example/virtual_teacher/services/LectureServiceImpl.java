package com.example.virtual_teacher.services;

import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.LectureDescription;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.repositories.contracts.LectureDescriptionRepository;
import com.example.virtual_teacher.repositories.contracts.LectureRepository;
import com.example.virtual_teacher.repositories.contracts.VideoRepository;
import com.example.virtual_teacher.services.contracts.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class LectureServiceImpl implements LectureService {
    private final LectureRepository lectureRepository;
    private final LectureDescriptionRepository lectureDescriptionRepository;
    private final VideoRepository videoRepository;
    public static final String UPDATE_LECTURE_ERROR_MESSAGE = "Only owner or admin can edit a lecture!";
    public static final String DELETE_LECTURE_ERROR_MESSAGE = "Only owner or admin can delete a lecture!";
    public static final String CREATE_LECTURE_ERROR_MESSAGE = "Only owner or admin can create a lecture!";

    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository, LectureDescriptionRepository lectureDescriptionRepository, VideoRepository videoRepository) {
        this.lectureRepository = lectureRepository;
        this.lectureDescriptionRepository = lectureDescriptionRepository;
        this.videoRepository = videoRepository;
    }

    @Override
    public List<Lecture> getAll() {
        return lectureRepository.getAll();
    }

    @Override
    public Lecture getById(int id) {
        Lecture lecture = lectureRepository.getById(id);
        if (lecture.getDescription() == null) {
            LectureDescription lectureDescription = new LectureDescription();
            lectureDescription.setDescription("No Description");
            lecture.setDescription(lectureDescription);
        }
        return lecture;
    }

    @Override
    public List<Lecture> getByCourseId(int id) {
        try {
            return lectureRepository.lecturesByCourseId(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Course", id);
        }
    }

    @Override
    public long lectureCount() {
        return lectureRepository.lectureCount();
    }

    @Override
    public Lecture create(Lecture lecture, User authUser) {
        if (!authUser.isAdmin() && !authUser.isTeacher()) {
            throw new UnauthorizedOperationException(CREATE_LECTURE_ERROR_MESSAGE);
        }
        boolean duplicateLectureTitleExists = true;
        try {
            lectureRepository.getByTitle(lecture.getTitle());
        } catch (EntityNotFoundException e) {
            duplicateLectureTitleExists = false;
        }
        if (duplicateLectureTitleExists) {
            throw new DuplicateEntityException("Lecture", "title",
                    lecture.getTitle());
        }
        if (lecture.getDescription() != null) {
            lectureDescriptionRepository.create(lecture.getDescription());
        }
        return lectureRepository.create(lecture);
    }

    @Override
    public Lecture update(Lecture lecture, User authUser) {
        if (!authUser.isAdmin() && authUser.getId() != lecture.getTeacher().getId()) {
            throw new UnauthorizedOperationException(UPDATE_LECTURE_ERROR_MESSAGE);
        }

        if ((lecture.isDeleted() && !authUser.isAdmin()) || ((authUser.getId() != lecture.getTeacher().getId()) && !authUser.isAdmin())) {
            throw new EntityNotFoundException("id", lecture.getId());
        }
        if (lecture.getDescription() != null) {
            if (lecture.getDescription().getId() != 0) {
                lectureDescriptionRepository.update(lecture.getDescription());
            }
            lectureDescriptionRepository.create(lecture.getDescription());
        }
        lectureRepository.update(lecture);
        return lecture;
    }

    @Override
    public Lecture delete(User authUser, int id) {
        Lecture lecture = lectureRepository.getById(id);
        if ((lecture.isDeleted() && !authUser.isAdmin()) || !authUser.isTeacher()) {
            throw new EntityNotFoundException("id", lecture.getId());
        }
        if (!authUser.isAdmin() && authUser.getId() != lecture.getTeacher().getId()) {
            throw new UnauthorizedOperationException(DELETE_LECTURE_ERROR_MESSAGE);
        }
        if (lecture.getDescription() != null) {
            lectureDescriptionRepository.delete(lecture.getDescription());
        }
        videoRepository.delete(lecture.getVideo().getVideoId());
        lectureRepository.delete(id);
        return lecture;
    }

    @Override
    public void saveFile(MultipartFile multipartFile, Lecture lecture) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(Paths.get("").toAbsolutePath()).append("\\src\\main\\resources\\static\\assignments\\");
        byte[] bytes = multipartFile.getBytes();
        sb.append(String.format("%d", lecture.getId()));
        Path path = Paths.get(sb + ".docx");
        Files.write(path, bytes);
    }

    @Override
    public String saveHomework(MultipartFile multipartFile, Lecture lecture, User authUser) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(Paths.get("").toAbsolutePath()).append("\\src\\main\\resources\\static\\homeworks\\");
        byte[] bytes = multipartFile.getBytes();
        String fileName = String.format("%s_%s_%d", authUser.getFirstName(),
                authUser.getLastName(), lecture.getId());
        sb.append(fileName);
        Path path = Paths.get(sb + ".docx");
        Files.write(path, bytes);
        return fileName;
    }

    @Override
    public List<Lecture> getAllByTeacherId(int id) {
        try {
            return lectureRepository.getAllByTeacherId(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Teacher", id, "lectures");
        }
    }
}
