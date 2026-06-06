package com.example.virtual_teacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.sql.Date;

@Entity
@Table(name = "ratings")
public class Rating implements PublicIdentifiable {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private int id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false, length = 36)
    private String publicId;

    @Basic
    @Column(name = "rating_score")
    private int ratingScore;

    @NotNull
    @Column(length = 1000, name = "review")
    private String review;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "course_id")
    private int courseId;

    @Column(name = "creation_date")
    private Date creationDate;

    public Rating() {
        creationDate = new Date(System.currentTimeMillis());
    }

    @PrePersist
    private void ensurePublicId() {
        PublicIdSupport.ensureAssigned(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getPublicId() {
        return publicId;
    }

    @Override
    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public int getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(int ratingScore) {
        this.ratingScore = ratingScore;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
