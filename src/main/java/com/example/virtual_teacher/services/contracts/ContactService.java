package com.example.virtual_teacher.services.contracts;

import com.example.virtual_teacher.models.ContactMessage;
import com.example.virtual_teacher.models.dtos.ContactMessageDto;

import java.util.List;

public interface ContactService {

    void submit(ContactMessageDto dto);

    List<ContactMessage> getAll();

    ContactMessage getByPublicId(String publicId);

    void markAsRead(String publicId);

    long countUnread();
}
