package com.example.virtual_teacher.repositories.contracts;

import com.example.virtual_teacher.models.Assignment;

public interface AssignmentRepository {

    Assignment getByAssignmentByLectureId(int lectureId);

    Assignment create(Assignment assignment);

    Assignment update(Assignment assignment);

    Assignment delete(int assignmentId);
}
