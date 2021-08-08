package com.example.board.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interface for working with storage.
 */
public interface StorageService {
    /**
     * Stores provided file on a local storage.
     * @param file A file provided
     * @return A string-path to uploaded file
     */
    String store(MultipartFile file);
}
