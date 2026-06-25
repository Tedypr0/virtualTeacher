package com.example.virtual_teacher.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_messages")
public class ContactMessage implements PublicIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_message_id")
    private int id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false, length = 36)
    private String publicId;

    @NotBlank
    @Column(name = "sender_name", nullable = false)
    private String senderName;

    @NotBlank
    @Column(name = "sender_email", nullable = false)
    private String senderEmail;

    @NotBlank
    @Column(name = "subject", nullable = false)
    private String subject;

    @NotBlank
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    public ContactMessage() {
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    @PrePersist
    private void ensurePublicId() {
        PublicIdSupport.ensureAssigned(this);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @Override
    public String getPublicId() { return publicId; }

    @Override
    public void setPublicId(String publicId) { this.publicId = publicId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
