package com.example.virtual_teacher.repositories.contracts;

import com.example.virtual_teacher.models.ContactMessage;

import java.util.List;

public interface ContactRepository {

    void save(ContactMessage message);

    List<ContactMessage> getAll();

    ContactMessage getByPublicId(String publicId);

    void markAsRead(String publicId);

    long countUnread();
}
