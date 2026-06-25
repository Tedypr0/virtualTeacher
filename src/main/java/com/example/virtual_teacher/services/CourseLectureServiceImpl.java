package com.example.virtual_teacher.services;

import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.CourseLecture;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.repositories.contracts.CourseLectureRepository;
import com.example.virtual_teacher.repositories.contracts.CourseRepository;
import com.example.virtual_teacher.repositories.contracts.LectureRepository;
import com.example.virtual_teacher.services.contracts.CourseLectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseLectureServiceImpl implements CourseLectureService {

    private final CourseLectureRepository courseLectureRepository;

    private final LectureRepository lectureRepository;

    private final CourseRepository courseRepository;

    @Autowired
    public CourseLectureServiceImpl(CourseLectureRepository courseLectureRepository, LectureRepository lectureRepository, CourseRepository courseRepository) {
        this.courseLectureRepository = courseLectureRepository;
        this.lectureRepository = lectureRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public void createOrDeleteCourseLecture(CourseLecture courseLecture) {
        Lecture lecture = lectureRepository.getById(courseLecture.getLectureId());
        Course course = courseRepository.getById(courseLecture.getCourseId());
        int courseId = course.getId();
        int lectureId = lecture.getId();

        if (lecture.isAddedToCourse()) {
            lecture.setAddedToCourse(false);
            if (courseLectureRepository.isAddedToCourse(courseId, lectureId)) {
                CourseLecture existing = courseLectureRepository.getByCourseAndLectureId(courseId, lectureId);
                courseLectureRepository.delete(existing.getId());
            }
        } else {
            lecture.setAddedToCourse(true);
            lecture.setCourse(course);
            if (!courseLectureRepository.isAddedToCourse(courseId, lectureId)) {
                CourseLecture link = new CourseLecture();
                link.setCourseId(courseId);
                link.setLectureId(lectureId);
                courseLectureRepository.create(link);
            }
        }
        lectureRepository.update(lecture);
    }

    @Override
    public boolean isAddedToCourse(int courseId, int lectureId) {
        return courseLectureRepository.isAddedToCourse(courseId, lectureId);
    }
}
