package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.exceptions.AuthenticationFailureException;
import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.Note;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.NoteDto;
import com.example.virtual_teacher.services.contracts.LectureService;
import com.example.virtual_teacher.services.contracts.NoteService;
import com.example.virtual_teacher.services.mappers.NoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class NoteMvcController {

    private final NoteService noteService;
    private final AuthenticationHelper authenticationHelper;
    private final NoteMapper noteMapper;
    private final LectureService lectureService;

    @Autowired
    public NoteMvcController(NoteService noteService, AuthenticationHelper authenticationHelper, NoteMapper noteMapper, LectureService lectureService) {
        this.noteService = noteService;
        this.authenticationHelper = authenticationHelper;
        this.noteMapper = noteMapper;
        this.lectureService = lectureService;
    }

    @PostMapping("courses/{courseId}/lectures{id}/notes/new")
    public String createNewNote(@PathVariable int courseId,
                                @PathVariable int id,
                                @Valid @ModelAttribute("note") NoteDto noteDto,
                                BindingResult errors,
                                Model model,
                                HttpSession session) {

        if (errors.hasErrors()) {
            return String.format("redirect:/courses/{courseId}/lectures/%d", courseId, id);
        }
        User authUser;
        try {
            Lecture lecture = lectureService.getById(id);
            authUser = authenticationHelper.tryGetUser(session);
            Note newNote = noteMapper.dtoToObj(noteDto, lecture, authUser);
            noteService.create(newNote);
            return String.format("redirect:/courses/{courseId}/lectures/%d", courseId, id);
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/courses/{courseId}/lectures/{lectureId}/notes/{noteId}/update")
    public String showEditNotePage(@PathVariable int courseId,
                                   @PathVariable int lectureId,
                                   @PathVariable int noteId,
                                   Model model,
                                   HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            Note note = noteService.getById(noteId);
            NoteDto noteDto = noteMapper.objToDto(note);
            model.addAttribute("note", noteDto);
            return "note-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/courses/{courseId}/lectures/{lectureId}/notes/{noteId}/update")
    public String updateNote(@PathVariable int courseId,
                             @PathVariable int lectureId,
                             @PathVariable int noteId,
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
            Note originalNote = noteService.getById(noteId);
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

    @GetMapping("/courses/{courseId}/lectures/{lectureId}/notes/{noteId}/delete")
    public String deleteNote(@PathVariable int courseId,
                             @PathVariable int lectureId,
                             @PathVariable int noteId,
                             Model model, HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            noteService.delete(authUser, noteId);
            return "redirect:/users/myNotes";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }
}
