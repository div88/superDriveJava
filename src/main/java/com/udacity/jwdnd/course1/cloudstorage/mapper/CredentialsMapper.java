package com.udacity.jwdnd.course1.cloudstorage.mapper;


import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialsMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    List<Credential> findCredentialsByUserId(@Param("userid") Integer userid);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    List<Credential> findById( Integer credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{credential.url}, #{credential.username}, #{credential.key}, #{credential.password}, #{credential.userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    Integer save(@Param("credential") Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{credential.url}, username = #{credential.username}, key = #{credential.key}, password = #{credential.password} WHERE credentialId = #{credential.credentialId}")
    void update(@Param("credential") Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    void delete(@Param("credentialId") Integer credentialId);
}
