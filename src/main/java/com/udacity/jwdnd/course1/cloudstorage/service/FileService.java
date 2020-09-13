package com.udacity.jwdnd.course1.cloudstorage.service;


import com.udacity.jwdnd.course1.cloudstorage.mapper.FilesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FilesMapper filesMapper;

    @Autowired
    public FileService(FilesMapper filesMapper){
        this.filesMapper = filesMapper;
    }

    public File uploadFile(MultipartFile multipartFile, Integer userId) throws IOException {
        File newFile = new File(multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                multipartFile.getSize(),
                multipartFile.getBytes(),
                userId);

        try{
            this.filesMapper.save(newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFile;
    }

    public List<File> getAllFiles(Integer userid) {
        return filesMapper.findFilesByUserId(userid);
    }

    public File getFileById(Integer fileId) {
        return filesMapper.findFileById(fileId);
    }

    public void delete(Integer fileId) throws IOException {
        try {
            filesMapper.delete(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isFileNameAvailable(MultipartFile file, Integer userId) {
        Boolean isFileNameAvailable = true;
        List <File> files = filesMapper.findFilesByUserId(userId);
        for(int i=0; i< files.size(); i++){
            File currentFile = files.get(i);
            if(currentFile.getFileName().equals(file.getOriginalFilename())) {
                isFileNameAvailable = false;
                break;
            }
        }
        return  isFileNameAvailable;
    }
}
