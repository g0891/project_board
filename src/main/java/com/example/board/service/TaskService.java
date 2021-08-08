package com.example.board.service;

import com.example.board.entity.task.TaskStatus;
import com.example.board.rest.dto.task.*;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Interface for working with tasks.
 */
public interface TaskService {
    /**
     * Searches and returns information about the task (if found).
     * @param id An id of task to search for
     * @return A task description
     */
    TaskReadDto getById(long id);

    /**
     * Retrieving a complete list of tasks.
     * @return A list of tasks
     */
    List<TaskReadDto> getAll();

    /*List<TaskReadDto> getFiltered(Long id,
                                  String name,
                                  String description,
                                  TaskStatus status,
                                  Long authorId,
                                  Long executorId,
                                  Long releaseId,
                                  Long projectId);*/

    /**
     * Returns a filtered list of tasks
     * @param taskSearchDto A DTO with filters to limit a tasks search
     * @return A list of tasks
     */
    List<TaskReadDto> getFiltered(TaskSearchDto taskSearchDto);

    /**
     * Used to add a task
     * @param task A DTO with parameters required to create a new task
     * @return An Id of task created
     */
    long add(TaskCreateDto task);

    /**
     * Used to add a task by uploading CSV file
     * @param file CSV file with task parameters
     * @return An Id of task created
     */
    long add(MultipartFile file);

    /*void update(long id,
                Optional<String> updatedName,
                Optional<String> updatedDescription,
                Optional<TaskStatus> updatedStatus,
                Optional<Long> updatedExecutorId) throws BoardAppIncorrectIdException;*/

    /**
     * Used to update task information.
     * @param id An id of task to search for
     * @param taskUpdateDto A DTO with parameters required to update a task
     */
    void update(long id, TaskUpdateDto taskUpdateDto);

    /**
     * Deletes a task from the system.
     * @param id An id of task to search for
     */
    void delete(long id);
}
