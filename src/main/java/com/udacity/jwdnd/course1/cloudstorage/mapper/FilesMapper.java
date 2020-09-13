package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface FilesMapper {
    @Select("SELECT * FROM FILES")
    List<File> findAll();

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    List<File> findFilesByUserId(@Param("userid") Integer userid);

    @Select("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES " +
            "(#{file.fileName}, #{file.contentType}, #{file.fileSize}, #{file.userId}, #{file.fileData})")
    Long save(@Param("file") File file);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File findFileById(@Param("fileId") Integer fileId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    Integer delete(@Param("fileId") Integer fileId);
}
