package com.example.virtual_teacher.services.mappers;

import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.CourseComment;
import com.example.virtual_teacher.models.Rating;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.CourseCommentDto;
import com.example.virtual_teacher.models.dtos.RatingDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RatingMapper {
    public RatingMapper() {
    }

    public Rating fromDto(RatingDto ratingDto, User user, Course course) {

        Rating rating = new Rating();

        rating.setRatingScore(ratingDto.getRating());
        rating.setReview(ratingDto.getReview());

        rating.setCourseId(course.getId());
        rating.setUser(user);
        rating.setCreationDate(ratingDto.getCreationDate());

        return rating;
    }

    public RatingDto objToDto(Rating rating) {
        List<Rating> ratings = new ArrayList<>();
        ratings.add(rating);
        return listObjToDto(ratings).get(0);
    }

    public List<RatingDto> listObjToDto(List<Rating> ratings){
        List<RatingDto> result = new ArrayList<>();
        for(Rating r: ratings) {
            RatingDto ratingDto = new RatingDto();
            ratingDto.setRating(r.getRatingScore());
            ratingDto.setReview(r.getReview());
            result.add(ratingDto);
        }
        return result;
    }

    public Rating dtoToObjForUpdate(Rating originalRating, RatingDto ratingDto) {
        originalRating.setRatingScore(ratingDto.getRating());
        originalRating.setReview(ratingDto.getReview());
        return originalRating;
    }
}
