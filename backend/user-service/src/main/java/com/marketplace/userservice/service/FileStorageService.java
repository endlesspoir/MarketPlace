package com.marketplace.userservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {

    String uploadFile(MultipartFile file, String key) ;

    void deleteFile(String key);

}