package com.marketplace.userservice.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service abstraction for file storage operations.
 *
 * Provides methods to upload and delete files from a storage backend (e.g., S3, local storage).
 */
public interface FileStorageService {

    /**
     * Uploads a file to the storage backend using the given key.
     *
     * @param file the MultipartFile to upload
     * @param key  the unique key or path where the file will be stored
     * @return the publicly accessible URL of the uploaded file
     * @throws RuntimeException if the file cannot be uploaded (e.g., IO error, storage error)
     */
    String uploadFile(MultipartFile file, String key);

    /**
     * Deletes a file from the storage backend by its key.
     *
     * @param key the unique key or path of the file to delete
     * @throws RuntimeException if deletion fails (e.g., file not found, storage error)
     */
    void deleteFile(String key);
}