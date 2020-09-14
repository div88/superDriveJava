package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CredentialController {
    private UserService userService;
    private NotesService notesService;
    private FileService fileService;
    private CredentialService credentialService;

    public CredentialController (UserService userService, FileService fileService, NotesService notesService, CredentialService credentialService) {
        this.userService = userService;
        this.fileService = fileService;
        this.notesService = notesService;
        this.credentialService = credentialService;
    }


    @PostMapping("/credential-upload")
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

        model.addAttribute("files", this.fileService.getAllFiles(userid));
        model.addAttribute("notes", notesService.getAllByUserId(userid));
        model.addAttribute("credentials", this.credentialService.getAllCredentials(userid));
        return "home";
    }

    @RequestMapping("/delete-credential/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") Integer credentialId, Model model) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUser(authentication.getName());
        Integer userid = currentUser.getUserId();

        String credentialDeleteError = null;
        try {
            this.credentialService.deleteCredential(credentialId);
            model.addAttribute("credentialDeleteSuccess", "Credential deleted successfully.");
        } catch (Exception e) {
            credentialDeleteError = e.toString();
            model.addAttribute("credentialError", credentialDeleteError);
        }

        model.addAttribute("files", this.fileService.getAllFiles(userid));
        model.addAttribute("notes", this.notesService.getAllByUserId(userid));
        model.addAttribute("credentials", this.credentialService.getAllCredentials(userid));
        return "home";
    }

    @GetMapping(value = "/decode-password")
    @ResponseBody
    public Map<String, String> decodePassword(@RequestParam Integer credentialId){
        Credential credential = credentialService.decodePassword(credentialId);
        String encryptedPassword = credential.getPassword();
        String encodedKey = credential.getKey();
        EncryptionService encryptionService = new EncryptionService();
        String decryptedPassword = encryptionService.decryptValue(encryptedPassword, encodedKey);
        Map<String, String> response = new HashMap<>();
        response.put("decryptedPassword", decryptedPassword);
        return response;
    }
}
