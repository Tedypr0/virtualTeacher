package com.example.virtual_teacher.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NoteDto {

    @NotBlank(message = "Note cannot be empty")
    private String note;

    public NoteDto() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
