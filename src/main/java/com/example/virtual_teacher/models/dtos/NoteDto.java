package com.example.virtual_teacher.models.dtos;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class NoteDto {

    @Column(name = "note")
    @NotNull
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
