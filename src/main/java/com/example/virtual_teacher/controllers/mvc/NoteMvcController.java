package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.exceptions.AuthenticationFailureException;
import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.Note;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.NoteDto;
import com.example.virtual_teacher.services.AccessControlService;
import com.example.virtual_teacher.services.contracts.CourseService;
import com.example.virtual_teacher.services.contracts.LectureService;
import com.example.virtual_teacher.services.contracts.NoteService;
import com.example.virtual_teacher.services.mappers.NoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/")
public class NoteMvcController {

    private final NoteService noteService;
    private final AuthenticationHelper authenticationHelper;
    private final AccessControlService accessControlService;
    private final NoteMapper noteMapper;
    private final LectureService lectureService;
    private final CourseService courseService;

    @Autowired
    public NoteMvcController(NoteService noteService,
                             AuthenticationHelper authenticationHelper,
                             AccessControlService accessControlService,
                             NoteMapper noteMapper,
                             LectureService lectureService,
                             CourseService courseService) {
        this.noteService = noteService;
        this.authenticationHelper = authenticationHelper;
        this.accessControlService = accessControlService;
        this.noteMapper = noteMapper;
        this.lectureService = lectureService;
        this.courseService = courseService;
    }

    @PostMapping("courses/{coursePublicId}/lectures/{lecturePublicId}/notes/new")
    public String createNewNote(@PathVariable String coursePublicId,
                                @PathVariable String lecturePublicId,
                                @Valid @ModelAttribute("note") NoteDto noteDto,
                                BindingResult errors,
                                Model model,
                                HttpSession session) {
        if (errors.hasErrors()) {
            return "redirect:/courses/" + coursePublicId + "/lectures/" + lecturePublicId;
        }
        try {
            User authUser = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(coursePublicId);
            Lecture lecture = lectureService.getByPublicId(lecturePublicId);
            accessControlService.assertCanViewLecture(authUser, course, lecture);
            Note existingNote = noteService.getByUserIdAndLectureId(authUser.getId(), lecture.getId());
            if (existingNote != null) {
                existingNote.setNote(noteDto.getNote());
                noteService.update(existingNote, authUser);
            } else {
                Note newNote = noteMapper.dtoToObj(noteDto, lecture, authUser);
                noteService.create(newNote);
            }
            return "redirect:/courses/" + coursePublicId + "/lectures/" + lecturePublicId;
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    @GetMapping("/courses/{coursePublicId}/lectures/{lecturePublicId}/notes/{notePublicId}/update")
    public String showEditNotePage(@PathVariable String coursePublicId,
                                   @PathVariable String lecturePublicId,
                                   @PathVariable String notePublicId,
                                   Model model,
                                   HttpSession session) {
        try {
            User authUser = authenticationHelper.tryGetUser(session);
            Note note = noteService.getByPublicId(notePublicId);
            accessControlService.assertCanViewNote(authUser, note);
            NoteDto noteDto = noteMapper.objToDto(note);
            model.addAttribute("note", noteDto);
            model.addAttribute("coursePublicId", coursePublicId);
            model.addAttribute("lecturePublicId", lecturePublicId);
            model.addAttribute("notePublicId", notePublicId);
            return "note-update";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    @PostMapping("/courses/{coursePublicId}/lectures/{lecturePublicId}/notes/{notePublicId}/update")
    public String updateNote(@PathVariable String coursePublicId,
                             @PathVariable String lecturePublicId,
                             @PathVariable String notePublicId,
                             @Valid @ModelAttribute("note") NoteDto noteDto,
                             BindingResult errors,
                             Model model,
                             HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "note-update";
        }

        try {
            Note originalNote = noteService.getByPublicId(notePublicId);
            accessControlService.assertCanModifyNote(authUser, originalNote);
            Note note = noteMapper.dtoToObjForUpdate(originalNote, noteDto);
            noteService.update(note, authUser);
            return "redirect:/users/myNotes";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("note", "duplicate_note", e.getMessage());
            return "note-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/courses/{coursePublicId}/lectures/{lecturePublicId}/notes/{notePublicId}/delete")
    public String deleteNote(@PathVariable String coursePublicId,
                             @PathVariable String lecturePublicId,
                             @PathVariable String notePublicId,
                             Model model,
                             HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
            Note note = noteService.getByPublicId(notePublicId);
            accessControlService.assertCanModifyNote(authUser, note);
            noteService.delete(authUser, note.getId());
            return "redirect:/users/myNotes";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }
}
