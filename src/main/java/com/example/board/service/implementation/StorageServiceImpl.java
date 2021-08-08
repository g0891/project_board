package com.example.board.service.implementation;

import com.example.board.rest.errorController.exception.BoardAppStorageException;
import com.example.board.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path storageLocation;
    private final boolean initialized;

    public StorageServiceImpl(@Value("${tmpFiles.path}") String path) {
        this.storageLocation = Paths.get(path);
        if (Files.notExists(storageLocation)) {
            try {
                Files.createDirectories(storageLocation);
            } catch (IOException e) {
                throw new BoardAppStorageException(String.format("Can't create temporary storage directory %s", path));
            }
        } else {
            if (!Files.isDirectory(storageLocation)) {
                throw new BoardAppStorageException(String.format("%s already exists and it's not a directory.", path));
            }
        }
        initialized = true;
    }

    @Override
    public String store(MultipartFile file) {
        if (!initialized) {
            throw new BoardAppStorageException("Can't store temporary files.");
        }

        if (file == null) {
            throw new BoardAppStorageException("File expected but null provided.");
        }

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new BoardAppStorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new BoardAppStorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.storageLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new BoardAppStorageException("Failed to store file " + filename, e);
        }

        return storageLocation.resolve(filename).toString();
    }
}
