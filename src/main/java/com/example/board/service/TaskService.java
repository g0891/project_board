package com.example.board.service;

import com.example.board.rest.dto.task.*;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    TaskReadDto getById(long id) throws BoardAppIncorrectIdException;
    List<TaskReadDto> getAll();
    long add(TaskCreateDto task) throws BoardAppIncorrectIdException;
/*    void update(long id, TaskUpdateDto task) throws BoardAppIncorrectIdException;*/
    void update(long id,
                Optional<String> updatedName,
                Optional<String> updatedDescription,
                Optional<TaskStatus> updatedStatus,
                Optional<Long> updatedExecutorId) throws BoardAppIncorrectIdException;
    void delete(long id) throws BoardAppIncorrectIdException;
}
