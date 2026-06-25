package com.example.virtual_teacher.services;

import com.example.virtual_teacher.models.ContactMessage;
import com.example.virtual_teacher.models.dtos.ContactMessageDto;
import com.example.virtual_teacher.repositories.contracts.ContactRepository;
import com.example.virtual_teacher.services.contracts.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void submit(ContactMessageDto dto) {
        ContactMessage message = new ContactMessage();
        message.setSenderName(dto.getSenderName());
        message.setSenderEmail(dto.getSenderEmail());
        message.setSubject(dto.getSubject());
        message.setMessage(dto.getMessage());
        contactRepository.save(message);
    }

    @Override
    public List<ContactMessage> getAll() {
        return contactRepository.getAll();
    }

    @Override
    public ContactMessage getByPublicId(String publicId) {
        return contactRepository.getByPublicId(publicId);
    }

    @Override
    public void markAsRead(String publicId) {
        contactRepository.markAsRead(publicId);
    }

    @Override
    public long countUnread() {
        return contactRepository.countUnread();
    }
}
