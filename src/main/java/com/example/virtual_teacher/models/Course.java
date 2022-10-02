package com.example.virtual_teacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private int id;

    @NotNull
    @Size(min = 5, max = 50, message = "Course title must be between 5 and 50 symbols")
    @Column(name = "title")
    private String title;

    @NotNull
    @OneToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @NotNull
    @Column(name = "avg_rating")
    private Double avgRating;

    @OneToOne
    @JoinColumn(name = "course_id")
    private CourseDescription description;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User teacher;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "lecture_id")
    private Set<Lecture> lectures;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    @OrderBy("creationDate")
    private Set<CourseComment> comments;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Set<UsersCourses> enroll;

    @NotNull
    @Column(name = "end_date")
    private Date endDate;

    @NotNull
    @Column(name = "starting_date")
    private Date startingDate;

    @JsonIgnore
    @Column(name = "is_draft")
    private boolean isDraft;

    @JsonIgnore
    @Column(name = "is_completed")
    private boolean isCompleted;

    @JsonIgnore
    @Column(name = "is_deleted")
    private boolean isDeleted;

    public Course() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public CourseDescription getDescription() {
        return description;
    }

    public void setDescription(CourseDescription description) {
        this.description = description;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public Set<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(Set<Lecture> lectures) {
        this.lectures = lectures;
    }

    public Set<CourseComment> getComments() {
        return comments;
    }

    public void setComments(Set<CourseComment> comments) {
        this.comments = comments;
    }

    public Set<UsersCourses> getEnroll() {
        return enroll;
    }

    public void setEnroll(Set<UsersCourses> enroll) {
        this.enroll = enroll;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public boolean isDraft() {
        return isDraft;
    }

    public void setDraft(boolean draft) {
        isDraft = draft;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getStatus(){
        if(isDraft){
            return "Draft";
        }
        return "Published";
    }
}
