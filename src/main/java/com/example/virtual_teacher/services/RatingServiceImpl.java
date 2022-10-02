package com.example.virtual_teacher.services;

import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.Rating;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.repositories.contracts.CourseRepository;
import com.example.virtual_teacher.repositories.contracts.RatingRepository;
import com.example.virtual_teacher.services.contracts.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class RatingServiceImpl implements RatingService {

    private final String CREATE_RATING_ERROR_MESSAGE = "You are not able to rate this course!";

    private final RatingRepository ratingRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, CourseRepository courseRepository) {
        this.ratingRepository = ratingRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public List<Rating> getAll() {
        return ratingRepository.getAll();
    }

    @Override
    public Rating getById(int id) {
        return ratingRepository.getById(id);
    }

    @Override
    public List<Rating> getByCourseId(int courseId) {
        return ratingRepository.getByCourseId(courseId);
    }

    //TODO VALIDATE USER
    @Override
    public Rating create(User user, Rating rating) {
        if (user.isAdmin() || user.isTeacher()) {
            throw new UnauthorizedOperationException(CREATE_RATING_ERROR_MESSAGE);}

        return ratingRepository.create(user, rating);
    }

    @Override
    public Rating update(User user, Rating rating) {
        return ratingRepository.update(user, rating);
    }

    @Override
    public Rating delete(User user, int courseId) {
        return ratingRepository.delete(user, courseId);
    }

    @Override
    public double getAvgRatingByCourseId(int courseId) {
        return ratingRepository.getAvgRatingByCourseId(courseId);
    }

    @Override
    public List<Double> updateAvgRatingOfCourses(List<Course> courses) {
        List<Double> courseRatings = new ArrayList<>();
        for (Course course : courses) {
            double rating = getAvgRatingByCourseId(course.getId());
            courseRatings.add(rating);
            course.setAvgRating(rating);
            courseRepository.update(course);
        }
        return courseRatings;
    }

    @Override
    public long getRatingsCount(int courseId) {
        return ratingRepository.getRatingsCount(courseId);
    }
}
