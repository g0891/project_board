package com.example.board.service.implementation;

import com.example.board.entity.PersonEntity;
import com.example.board.entity.ReleaseEntity;
import com.example.board.entity.TaskEntity;
import com.example.board.mapper.PersonMapper;
import com.example.board.mapper.TaskMapper;
import com.example.board.repository.PersonRepository;
import com.example.board.repository.ReleaseRepository;
import com.example.board.repository.TaskRepository;
import com.example.board.rest.dto.person.PersonRole;
import com.example.board.rest.dto.release.ReleaseStatus;
import com.example.board.rest.dto.task.*;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectRoleException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectStateException;
import com.example.board.rest.errorController.exception.BoardAppPermissionViolationException;
import com.example.board.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final PersonRepository personRepository;
    private final TaskMapper taskMapper;
    private final PersonMapper personMapper;

    //@Autowired
    public TaskServiceImpl(TaskRepository taskRepository, PersonRepository personRepository, TaskMapper taskMapper, PersonMapper personMapper) {
        this.taskRepository = taskRepository;
        this.personRepository = personRepository;
        this.taskMapper = taskMapper;
        this.personMapper = personMapper;
    }

    @Override
    public TaskReadDto getById(long id) throws BoardAppIncorrectIdException {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no task with id = %d", id))
        );
        return taskMapper.taskEntityToTaskReadDto(taskEntity);
    }

    @Override
    public List<TaskReadDto> getAll() {
        List<TaskEntity> taskEntities = taskRepository.findAll();
        return taskMapper.taskEntityListToTaskReadDtoList(taskEntities);
    }

    @Override
    public long add(TaskCreateDto task) throws BoardAppIncorrectIdException {
        TaskEntity taskEntity = taskMapper.taskCreateDtoToTaskEntity(task);

        if (taskEntity.getRelease().getStatus() != ReleaseStatus.OPEN) {
            throw new BoardAppIncorrectStateException("A release should be in OPEN state to add a task. Task not added.");
        }

        if (!personMapper.roleEntitySetToPersonRoleSet(taskEntity.getAuthor().getRoles()).contains(PersonRole.AUTHOR)) {
            throw new BoardAppIncorrectRoleException("Only person with AUTHOR role can be an author of the task.");
        }

        taskEntity = taskRepository.save(taskEntity);
        return taskEntity.getId();
    }

    @Override
    public void update(long id,
                       Optional<String> updatedName,
                       Optional<String> updatedDescription,
                       Optional<TaskStatus> updatedStatus,
                       Optional<Long> updatedExecutorId) throws BoardAppIncorrectIdException {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no task with id = %d", id))
        );

        if (taskEntity.getStatus() == TaskStatus.DONE || taskEntity.getStatus() == TaskStatus.CANCELED) {
            throw new BoardAppIncorrectStateException("Task in DONE or CANCELED state  can't be modified");
        }

        updatedName.ifPresent(taskEntity::setName);
        updatedDescription.ifPresent(taskEntity::setDescription);

        if (updatedStatus.isPresent()) {
            TaskStatus updatedStatusValue = updatedStatus.get();
            if (taskEntity.getStatus() == TaskStatus.BACKLOG
                    && updatedStatusValue != TaskStatus.IN_PROGRESS
                    && updatedStatusValue != TaskStatus.CANCELED) {
                throw new BoardAppIncorrectStateException("Task in BACKLOG status can be moved to IN_PROGRESS or CANCELED status only.");
            }

            if (taskEntity.getStatus() == TaskStatus.IN_PROGRESS
                    && updatedStatusValue != TaskStatus.DONE
                    && updatedStatusValue != TaskStatus.CANCELED) {
                throw new BoardAppIncorrectStateException("Task in IN_PROGRESS status can be moved to DONE or CANCELED status only.");
            }

            if (taskEntity.getStatus() == TaskStatus.BACKLOG
                    && updatedStatusValue == TaskStatus.IN_PROGRESS
                    && updatedExecutorId.isEmpty()) {
                throw new BoardAppIncorrectStateException("Task can't be moved to IN_PROGRESS without defining an executor");
            }

            if (updatedStatusValue == TaskStatus.CANCELED || updatedStatusValue == TaskStatus.DONE) {
                taskEntity.setDoneOn(LocalDateTime.now());
            }

            taskEntity.setStatus(updatedStatusValue);
        }

        if (updatedExecutorId.isPresent()) {
            if (taskEntity.getStatus() != TaskStatus.IN_PROGRESS && taskEntity.getStatus() != TaskStatus.BACKLOG) {
                throw new BoardAppIncorrectStateException("Can't set an executor for task in a status other than IN_PROGRESS or BACKLOG.");
            }
            PersonEntity executor = personRepository.findById(updatedExecutorId.get()).orElseThrow(
                    () -> new BoardAppIncorrectIdException(String.format("There is no person with id = %d", updatedExecutorId.get()))
            );

            if (!personMapper.roleEntitySetToPersonRoleSet(executor.getRoles()).contains(PersonRole.EXECUTOR)) {
                throw new BoardAppIncorrectRoleException("A person can't be set as an executor for a task because the EXECUTOR role is missing.");
            }

            taskEntity.setExecutor(executor);
        }

        taskRepository.save(taskEntity);
    }

    @Override
    public void delete(long id) throws BoardAppIncorrectIdException {
        Optional<TaskEntity> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            throw new BoardAppIncorrectIdException(String.format("There is no task with id = %d", id));
        }

        if (task.get().getStatus() != TaskStatus.BACKLOG) {
            throw new BoardAppIncorrectStateException("Only task in BACKLOG status can be deleted. Otherwise please complete or cancel task instead.");
        }

        taskRepository.deleteById(id);
    }
}
