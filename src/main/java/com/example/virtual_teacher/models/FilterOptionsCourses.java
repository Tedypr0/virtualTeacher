package com.example.virtual_teacher.models;
import java.util.Optional;

public class FilterOptionsCourses {
    private Optional<String> title;
    private Optional<Integer> topicId;
    private Optional<Integer> teacherId;
    private Optional<Double> rating;

    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterOptionsCourses() {
    }

    public FilterOptionsCourses(Optional<String> title,
                                Optional<Integer> topicId,
                                Optional<Integer> teacherId,
                                Optional<Double> rating,
                                Optional<String> sortBy,
                                Optional<String> sortOrder) {
        this.title = title;
        this.topicId = topicId;
        this.teacherId = teacherId;
        this.rating = rating;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<Integer> getTopicId() {
        return topicId;
    }

    public Optional<Integer> getTeacherId() {
        return teacherId;
    }

    public Optional<Double> getRating() {
        return rating;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
