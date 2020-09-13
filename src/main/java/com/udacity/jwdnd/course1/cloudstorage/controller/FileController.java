package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@Controller
public class FileController {
    private UserService userService;
    private FileService fileService;

    public FileController (UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @RequestMapping(value = "/file-upload", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Model model) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUser(authentication.getName());
        Integer userid = currentUser.getUserId();

        String fileUploadError = null;
        if(this.fileService.isFileNameAvailable(fileUpload, currentUser.getUserId())) {
            try {
                this.fileService.uploadFile(fileUpload, userid);
                model.addAttribute("fileUploadSuccess", "File successfully uploaded.");
            } catch (Exception e) {
                fileUploadError = e.toString();
                model.addAttribute("fileError", fileUploadError);
            }
        } else {
            model.addAttribute("fileError", "Can't upload files with duplicate names.");
        }

        model.addAttribute("files", this.fileService.getAllFiles(currentUser.getUserId()));
        return "home";
    }

    @RequestMapping("/file/view/{fileId}")
    public ResponseEntity downloadFile(@PathVariable("fileId") Integer fileId,
                                       Authentication authentication) throws IOException {
        File file = fileService.getFileById(fileId);
        String contentType = file.getContentType();
        String fileName = file.getFileName();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file.getFileData());
    }

    @RequestMapping("/file-delete/{fileid}")
    public String delete(@PathVariable("fileid") Integer fileid,
                             Authentication authentication,
                             Model model) throws IOException {
        String fileDeleteError = null;
        User currentUser = userService.getUser(authentication.getName());
        Integer userid = currentUser.getUserId();

        try {
            fileService.delete(fileid);
            model.addAttribute("fileDeleteSuccess", "File successfully deleted.");
        } catch (Exception e) {
            fileDeleteError = e.toString();
            model.addAttribute("fileError", fileDeleteError);
        }
        model.addAttribute("files", this.fileService.getAllFiles(userid));
        return "home";
    }
}
