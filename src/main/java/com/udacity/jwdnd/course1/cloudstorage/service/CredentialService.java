package com.udacity.jwdnd.course1.cloudstorage.service;


import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialsMapper credentialsMapper;

    @Autowired
    public CredentialService(CredentialsMapper credentialsMapper) {
        this.credentialsMapper = credentialsMapper;
    }

    public Credential addCredential(String url, String userName, String password, Integer userid) throws IOException {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        EncryptionService encryptionService = new EncryptionService();
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        Credential newCredential = new Credential(url, userName, encryptedPassword, encodedKey, userid);

        try {
            credentialsMapper.save(newCredential);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newCredential;
    }

    public Credential updateCredential(Integer credentialId, String url, String username, String password, Integer userid) throws IOException {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        EncryptionService encryptionService = new EncryptionService();
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        Credential newCredential = new Credential(credentialId, url, username, encryptedPassword, encodedKey, userid);

        try {
            credentialsMapper.update(newCredential);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newCredential;
    }

    public List<Credential> getAllCredentials(Integer userId) {
        return credentialsMapper.findCredentialsByUserId(userId);
    }

    public void deleteCredential(Integer credentialId) throws IOException {
        try {
            credentialsMapper.delete(credentialId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Credential decodePassword(Integer credentialId){
        return (Credential) credentialsMapper.findById(credentialId);
    }
}
