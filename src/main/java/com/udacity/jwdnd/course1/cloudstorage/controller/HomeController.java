package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private NotesService notesService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String homePageView(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        model.addAttribute("notes", notesService.getAllByUserId(user.getUserId()));
        return "home";
    }
}
