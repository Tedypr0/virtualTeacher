package com.example.virtual_teacher.repositories.contracts;

import com.example.virtual_teacher.models.ContactMessage;

import java.util.List;
import java.util.Set;

public interface ContactRepository {

    void save(ContactMessage message);

    List<ContactMessage> getAll();

    ContactMessage getByPublicId(String publicId);

    void markAsRead(String publicId, int userId);

    long countUnread(int userId);

    Set<String> getReadPublicIds(int userId);
}
