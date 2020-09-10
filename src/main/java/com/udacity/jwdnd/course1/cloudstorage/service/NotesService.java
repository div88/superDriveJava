package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {
    private NotesMapper notesMapper;

    @Autowired
    public NotesService(NotesMapper notesMapper){
        this.notesMapper = notesMapper;
    }

    public List<Note> getAll() {
        return notesMapper.findAll();
    }

    public List<Note> getAllByUserId(Integer userid) {
        return notesMapper.findByUserId(userid);
    }

    public Note getById(Integer id, Integer userid) {
        return notesMapper.findById(id, userid);
    }

    public Boolean noteExists(Integer id, Integer userid) throws Exception {
        try {
            Note note = notesMapper.findById(id, userid);
            if (note == null) {
                return false;
            }
            return true;
        }  catch (Exception e) {
            throw e;
        }
    }

    public boolean create(Note note, Integer userid) {
        return notesMapper.create(note, userid) > 0;
    }

    public boolean update(Note note, Integer userid) {
        return notesMapper.update(note, userid) > 0;
    }

    public boolean delete(Integer id, Integer userid) {
        return notesMapper.delete(id, userid) > 0;
    }
}
