package com.example.virtual_teacher.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContactMessageDto {

    @NotBlank(message = "Name is required.")
    @Size(max = 100, message = "Name must be under 100 characters.")
    private String senderName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    private String senderEmail;

    @NotBlank(message = "Subject is required.")
    @Size(max = 200, message = "Subject must be under 200 characters.")
    private String subject;

    @NotBlank(message = "Message is required.")
    @Size(min = 10, max = 2000, message = "Message must be between 10 and 2000 characters.")
    private String message;

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
