package com.example.virtual_teacher.models.dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.sql.Date;

public class RatingDto {
    private int courseId;

    private int userId;

    @Min(value = 1, message = "Rating cannot be less than 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private int rating;

    @Size(min = 75, max = 1000, message = "Review should be a between 75 and 1000 symbols")
    private String review;

    private Date creationDate;

    public RatingDto() {
        creationDate = new Date(System.currentTimeMillis());
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
