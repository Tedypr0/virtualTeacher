package com.example.virtual_teacher.repositories.contracts;

import com.example.virtual_teacher.models.Rating;
import com.example.virtual_teacher.models.User;

import java.util.List;

public interface RatingRepository {

    List<Rating> getAll();

    Rating getById(int id);

    List<Rating> getByCourseId(int courseId);

    Rating create(User user, Rating rating);

    Rating update(User user, Rating rating);

    Rating delete(User user, int courseId);

    double getAvgRatingByCourseId(int courseId);

    long getRatingsCount(int courseId);
}
