package com.example.board.service;

import com.example.board.rest.dto.task.TaskCreateDto;
import com.example.board.rest.dto.task.TaskReadDto;
import com.example.board.rest.dto.task.TaskUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TaskService {
    TaskReadDto getById(long id) throws BoardAppIncorrectIdException;
    List<TaskReadDto> getAll();
    long add(TaskCreateDto task) throws BoardAppIncorrectIdException;
    void update(long id, TaskUpdateDto task) throws BoardAppIncorrectIdException;
    void delete(long id) throws BoardAppIncorrectIdException;
}
