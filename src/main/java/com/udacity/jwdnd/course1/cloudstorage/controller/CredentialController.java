package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class CredentialController {
    private UserService userService;
    private CredentialService credentialService;

    public CredentialController (UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }


    @PostMapping("/credential-upload")
//    public String addCredential(Integer credentialId, String credentialUrl, String credentialUsername, String credentialPassword, Model model) throws IOException {
    public String uploadCredential(@RequestParam("credentialId") Integer credentialId,
                @RequestParam("url") String credentialUrl,
                @RequestParam("username") String credentialUsername,
                @RequestParam("password") String credentialPassword,
                Model model) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUser(authentication.getName());
        Integer userid = currentUser.getUserId();

        String credentialEditError = null;
        String credentialAddError = null;

        if(credentialId == null) {
            try {
                System.out.println("A" + credentialUrl);
                System.out.println(credentialUsername);
                System.out.println(credentialPassword);
                System.out.println(userid);
                this.credentialService.addCredential(credentialUrl, credentialUsername, credentialPassword, userid);
                model.addAttribute("credentialUploadSuccess", "Credential Added Successfully.");
            } catch (Exception e) {
                credentialAddError = e.toString();
                model.addAttribute("credentialError", credentialAddError);
            }
        } else {
            try {
                this.credentialService.updateCredential(credentialId,
                        credentialUrl,
                        credentialUsername,
                        credentialPassword,
                        userid);
                model.addAttribute("credentialEditSuccess", "Credential Updated Successfully.");
            } catch(Exception e) {
                credentialEditError = e.toString();
                model.addAttribute("credentialError", credentialEditError);
            }
        }

        model.addAttribute("credentials", this.credentialService.getAllCredentials(userid));
        System.out.println("XXXX" + this.credentialService.getAllCredentials(userid));
        return "home";
    }
}
