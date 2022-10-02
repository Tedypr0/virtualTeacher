package com.example.virtual_teacher.services;

import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.LectureComment;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.repositories.contracts.LectureCommentRepository;
import com.example.virtual_teacher.services.contracts.LectureCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureCommentServiceImpl implements LectureCommentService {

    private final LectureCommentRepository lectureCommentRepository;

    public static final String MODIFY_COMMENT_ERROR_MESSAGE = "You can only %s your own comments!";

    @Autowired
    public LectureCommentServiceImpl(LectureCommentRepository lectureCommentRepository) {
        this.lectureCommentRepository = lectureCommentRepository;
    }

    @Override
    public List<LectureComment> getAll(int lectureId) {
        return lectureCommentRepository.getAll(lectureId);
    }

    @Override
    public LectureComment getById(int id) {
        return lectureCommentRepository.getById(id);
    }

    @Override
    public LectureComment create(LectureComment lectureComment) {
        lectureCommentRepository.create(lectureComment);
        return lectureComment;
    }

    @Override
    public LectureComment update(User authUser, LectureComment lectureComment) {
        if (!authUser.isAdmin() && authUser.getId() != lectureComment.getUser().getId()) {
            throw new UnauthorizedOperationException(String.format(MODIFY_COMMENT_ERROR_MESSAGE,"update"));
        }
        return lectureCommentRepository.update(lectureComment);
    }

    @Override
    public LectureComment delete(User authUser, int id) {
        LectureComment commentToDelete = lectureCommentRepository.getById(id);
        if (!authUser.isAdmin() && authUser.getId() != commentToDelete.getUser().getId()) {
            throw new UnauthorizedOperationException(String.format(MODIFY_COMMENT_ERROR_MESSAGE,"delete"));
        }
        lectureCommentRepository.delete(id);
        return commentToDelete;
    }
}
