package com.marketplace.userservice.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.marketplace.userservice.exception.FileStorageException;
import com.marketplace.userservice.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "s3")
@RequiredArgsConstructor
public class S3FileStorageServiceImpl implements FileStorageService {

    private final AmazonS3Client amazonS3Client;
    private final String amazonBucket;


    @Override
    public String uploadFile(MultipartFile file, String key)  {



        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(file.getContentType());
        meta.setContentLength(file.getSize());

        try {
            amazonS3Client.putObject(amazonBucket,key,file.getInputStream(),meta);
        }catch (IOException ex){
            throw new FileStorageException("Failed to upload file to S3/MinIO for key: " + key);
        }

        return amazonS3Client.getUrl(amazonBucket, key).toString();
    }

    public void deleteFile(String key){
        amazonS3Client.deleteObject(amazonBucket, key);
    }
}
