package com.example.board.service;

import com.example.board.entity.task.TaskStatus;
import com.example.board.rest.dto.task.*;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    TaskReadDto getById(long id) throws BoardAppIncorrectIdException;
    List<TaskReadDto> getAll();
    /*List<TaskReadDto> getFiltered(Long id,
                                  String name,
                                  String description,
                                  TaskStatus status,
                                  Long authorId,
                                  Long executorId,
                                  Long releaseId,
                                  Long projectId);*/
    List<TaskReadDto> getFiltered(TaskSearchDto taskSearchDto);
    long add(TaskCreateDto task) throws BoardAppIncorrectIdException;
    /*void update(long id,
                Optional<String> updatedName,
                Optional<String> updatedDescription,
                Optional<TaskStatus> updatedStatus,
                Optional<Long> updatedExecutorId) throws BoardAppIncorrectIdException;*/
    void update(long id, TaskUpdateDto taskUpdateDto);
    void delete(long id) throws BoardAppIncorrectIdException;
}
