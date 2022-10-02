package com.example.virtual_teacher.models.dtos;

import javax.validation.constraints.Size;

public class CourseCommentDto {
    @Size(max = 900, message = "Comment content must be up to and 900 symbols")
    private String content;

    public CourseCommentDto() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
