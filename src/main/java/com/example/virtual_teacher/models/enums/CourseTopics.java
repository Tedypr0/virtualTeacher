package com.example.virtual_teacher.models.enums;

public enum CourseTopics {
    SCIENCE,
    HISTORY,
    LITERATURE,
    PHOTOGRAPHY,
    PROGRAMMING;

    @Override
    public String toString() {
        switch (this) {
            case SCIENCE:
                return "Science";
            case HISTORY:
                return "History";
            case LITERATURE:
                return "Literature";
            case PHOTOGRAPHY:
                return "Photography";
            case PROGRAMMING:
                return "Programming";
            default:
                return "Invalid topic";
        }
    }
}
