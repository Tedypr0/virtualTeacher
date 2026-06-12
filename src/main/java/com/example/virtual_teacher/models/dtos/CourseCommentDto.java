package com.example.virtual_teacher.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CourseCommentDto {
    @NotBlank(message = "Comment cannot be empty")
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
