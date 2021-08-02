package com.example.board.service.implementation;

import com.example.board.entity.PersonEntity;
import com.example.board.entity.ReleaseEntity;
import com.example.board.entity.TaskEntity;
import com.example.board.mapper.TaskMapper;
import com.example.board.repository.PersonRepository;
import com.example.board.repository.ReleaseRepository;
import com.example.board.repository.TaskRepository;
import com.example.board.rest.dto.task.TaskCreateDto;
import com.example.board.rest.dto.task.TaskReadDto;
import com.example.board.rest.dto.task.TaskUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ReleaseRepository releaseRepository;

    @Autowired
    TaskMapper taskMapper;

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
        taskEntity = taskRepository.save(taskEntity);
        return taskEntity.getId();
    }

    @Override
    public void update(long id, TaskUpdateDto task) throws BoardAppIncorrectIdException {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no task with id = %d", id))
        );

        PersonEntity author = personRepository.findById(task.getAuthorId()).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no person with id = %d", task.getAuthorId()))
        );

        PersonEntity executor;
        if (task.getExecutorId() == null) {
            executor = null;
        } else {
            executor = personRepository.findById(task.getExecutorId()).orElseThrow(
                    () -> new BoardAppIncorrectIdException(String.format("There is no person with id = %d", task.getExecutorId()))
            );
        }

        ReleaseEntity releaseEntity = releaseRepository.findById(task.getReleaseId()).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no release with id = %d", task.getReleaseId()))
        );

        taskEntity.setName(task.getName());
        taskEntity.setDescription(task.getDescription());
        taskEntity.setStatus(task.getStatus());
        taskEntity.setAuthor(author);
        taskEntity.setExecutor(executor);
        taskEntity.setRelease(releaseEntity);
        taskRepository.save(taskEntity);

    }

    @Override
    public void delete(long id) throws BoardAppIncorrectIdException {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new BoardAppIncorrectIdException(String.format("There is no task with id = %d", id));
        }
    }
}
