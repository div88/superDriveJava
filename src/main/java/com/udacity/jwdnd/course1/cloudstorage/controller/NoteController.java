package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NoteController {
    private final UserService userService;
    private final NotesService notesService;

    public NoteController(UserService userService, NotesService notesService) {
        this.userService = userService;
        this.notesService = notesService;
    }

    @PostMapping(value = "/addNote")
    public String addNote(Note note, Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userService.getUser(authentication.getName());
            Integer userid = currentUser.getUserId();
            Integer noteid = note.getNoteId();

            if (noteid == null) {
                notesService.create(note, userid);
            } else {
                notesService.update(note, userid);
            }

            model.addAttribute("success", true);
            return "redirect:/result?success";
        } catch (Exception e) {
            model.addAttribute("error", true);
            return "redirect:/result?error";
        }
    }


    @GetMapping(value = "/delete-note/{id}")
    public String deleteNote(@PathVariable("id") Integer id, Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userService.getUser(authentication.getName());
            Integer userid = currentUser.getUserId();
            if (notesService.noteExists(id, userid)) {
                notesService.delete(id, userid);
                model.addAttribute("success", true);
                return "result";
            }
            model.addAttribute("error", true);
            return "result";
        } catch (Exception e) {
            model.addAttribute("error", true);
            return "result";
        }
    }
}

