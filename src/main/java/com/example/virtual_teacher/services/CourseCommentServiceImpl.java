package com.example.virtual_teacher.services;

import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.CourseComment;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.repositories.contracts.CourseCommentRepository;
import com.example.virtual_teacher.services.contracts.CourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseCommentServiceImpl implements CourseCommentService {

    private final CourseCommentRepository courseCommentRepository;

    public static final String MODIFY_COMMENT_ERROR_MESSAGE = "You can only %s your own comments!";

    @Autowired
    public CourseCommentServiceImpl(CourseCommentRepository courseCommentRepository) {
        this.courseCommentRepository = courseCommentRepository;
    }


    @Override
    public List<CourseComment> getAll() {
        return courseCommentRepository.getAll();
    }

    @Override
    public CourseComment getById(int id) {
        return courseCommentRepository.getById(id);
    }

    @Override
    public CourseComment create(CourseComment courseComment) {
        courseCommentRepository.create(courseComment);
        return courseComment;
    }

    @Override
    public CourseComment update(User authUser, CourseComment courseComment) {
        if (!authUser.isAdmin() && authUser.getId() != courseComment.getUser().getId()) {
            throw new UnauthorizedOperationException(String.format(MODIFY_COMMENT_ERROR_MESSAGE, "update"));
        }
        return courseCommentRepository.update(courseComment);
    }

    @Override
    public CourseComment delete(User authUser, int id) {
        CourseComment commentToDelete = courseCommentRepository.getById(id);
        if (!authUser.isAdmin() && authUser.getId() != commentToDelete.getUser().getId()) {
            throw new UnauthorizedOperationException(String.format(MODIFY_COMMENT_ERROR_MESSAGE, "delete"));
        }
        courseCommentRepository.delete(id);
        return commentToDelete;
    }

    @Override
    public List<CourseComment> getByCourseId(int courseId) {
        return courseCommentRepository.getByCourseId(courseId);
    }

    @Override
    public long getCommentsCount() {
        return courseCommentRepository.getCourseCommentsCount();
    }
}
