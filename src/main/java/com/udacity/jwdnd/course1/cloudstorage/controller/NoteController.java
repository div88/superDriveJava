package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class NoteController {
    private UserService userService;
    private NotesService notesService;
    private FileService fileService;
    private CredentialService credentialService;

    public NoteController (UserService userService, FileService fileService, NotesService notesService, CredentialService credentialService) {
        this.userService = userService;
        this.fileService = fileService;
        this.notesService = notesService;
        this.credentialService = credentialService;
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
            model.addAttribute("notes", notesService.getAllByUserId(userid));
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
                model.addAttribute("success", "Note deleted");
                return "redirect:/home";
            }
            model.addAttribute("error", true);
            return "redirect:/result?error";
        } catch (Exception e) {
            model.addAttribute("error", true);
            return "redirect:/result?error";
        }
    }
}

