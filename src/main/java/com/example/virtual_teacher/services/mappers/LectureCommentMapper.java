package com.example.virtual_teacher.services.mappers;

import com.example.virtual_teacher.models.LectureComment;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.LectureCommentDto;
import org.springframework.stereotype.Component;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;

@Component
public class LectureCommentMapper {


    public LectureComment dtoToObj(LectureCommentDto lectureCommentDto, int lectureId, User authUser) {
        LectureComment lectureComment = new LectureComment();
        lectureComment.setLectureId(lectureId);
        lectureComment.setUser(authUser);
        lectureComment.setContent(lectureCommentDto.getContent());
        return lectureComment;
    }

    public LectureCommentDto objToDto(LectureComment lectureComment) {
            List<LectureComment> lectureComments = new ArrayList<>();
            lectureComments.add(lectureComment);
            return listObjToDto(lectureComments).get(0);
        }

    public List<LectureCommentDto> listObjToDto(List<LectureComment> lectureComments){
        List<LectureCommentDto> result = new ArrayList<>();
        for(LectureComment c: lectureComments) {
            LectureCommentDto lectureCommentDto = new LectureCommentDto();
            lectureCommentDto.setContent(c.getContent());
            result.add(lectureCommentDto);
        }
        return result;
    }

    public LectureComment dtoToObjForUpdate(LectureComment originalComment, LectureCommentDto lectureCommentDto) {
        originalComment.setContent(lectureCommentDto.getContent());
        return originalComment;
    }

}
