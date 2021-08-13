package com.example.board.service.implementation;

import com.example.board.entity.person.PersonEntity;
import com.example.board.entity.release.ReleaseStatus;
import com.example.board.entity.role.PersonRole;
import com.example.board.entity.task.TaskEntity;
import com.example.board.entity.task.TaskStatus;
import com.example.board.mapper.PersonMapper;
import com.example.board.mapper.TaskMapper;
import com.example.board.repository.PersonRepository;
import com.example.board.repository.TaskRepository;
import com.example.board.rest.dto.task.TaskCreateDto;
import com.example.board.rest.dto.task.TaskReadDto;
import com.example.board.rest.dto.task.TaskSearchDto;
import com.example.board.rest.dto.task.TaskUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectRoleException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectStateException;
import com.example.board.rest.errorController.exception.BoardAppStorageException;
import com.example.board.service.StorageService;
import com.example.board.service.TaskService;
import com.example.board.service.specification.TaskSpecification;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final PersonRepository personRepository;
    private final TaskMapper taskMapper;
    private final PersonMapper personMapper;
    private final StorageService storageService;


    //@Autowired
    public TaskServiceImpl(TaskRepository taskRepository, PersonRepository personRepository, TaskMapper taskMapper, PersonMapper personMapper, StorageService storageService) {
        this.taskRepository = taskRepository;
        this.personRepository = personRepository;
        this.taskMapper = taskMapper;
        this.personMapper = personMapper;
        this.storageService = storageService;
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

   /* @Override
    public List<TaskReadDto> getFiltered(Long id,
                                         String name,
                                         String description,
                                         TaskStatus status,
                                         Long authorId,
                                         Long executorId,
                                         Long releaseId,
                                         Long projectId) {
        List<TaskEntity> taskEntityList = taskRepository.findAll(TaskSpecification.get(id,name,description,status,authorId,executorId,releaseId,projectId));
        return taskMapper.taskEntityListToTaskReadDtoList(taskEntityList);
    }*/

    public List<TaskReadDto> getFiltered(TaskSearchDto taskSearchDto) {
        List<TaskEntity> taskEntityList = taskRepository.findAll(TaskSpecification.get(taskSearchDto));
        return taskMapper.taskEntityListToTaskReadDtoList(taskEntityList);
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
    public long add(MultipartFile file) {

        String filepath = storageService.store(file);

        try (Reader in = new FileReader(filepath)){
            CSVFormat format = CSVFormat.Builder
                    .create(CSVFormat.RFC4180)
                    .setHeader(TaskCreateDto.fields)
                    .build();

            Iterable<CSVRecord> records = format.parse(in);
            Iterator<CSVRecord> iterator = records.iterator();
            if (iterator.hasNext()) {
                CSVRecord record = iterator.next();

                String name = record.get("name");
                if (name == null || name.isEmpty()) {
                    throw new IllegalArgumentException("Task name can't be an empty string");
                }

                String description = record.get("description");
                if (description == null || description.isEmpty()) {
                    throw new IllegalArgumentException("Task description can't be an empty string");
                }

                long authorId;
                String authorIdString = record.get("authorId");
                if (authorIdString == null || authorIdString.isEmpty()) {
                    throw new IllegalArgumentException("Task should have an author defined");
                } else {
                    try {
                        authorId = Long.parseLong(authorIdString);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Author id should have a valid number format.");
                    }
                }

                long releaseId;
                String releaseIdString = record.get("releaseId");
                if (releaseIdString == null || releaseIdString.isEmpty()) {
                    throw new IllegalArgumentException("Task should have a release defined");
                } else {
                    try {
                        releaseId = Long.parseLong(releaseIdString);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Release id should have a valid number format.");
                    }
                }

                Long executorId;
                String executorIdString = record.get("executorId");
                if (executorIdString == null || executorIdString.isEmpty()) {
                    executorId = null;
                } else {
                    try {
                        executorId = Long.parseLong(executorIdString);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Executor id should have a valid number format.");
                    }
                }

                return this.add(new TaskCreateDto(name, description, authorId, executorId, releaseId));
            } else {
                throw new IllegalArgumentException("There are no records in CSV file");
            }
        } catch (FileNotFoundException ex) {
            throw new BoardAppStorageException("Uploaded file can't be founded.");
        } catch (IOException ex) {
            throw new BoardAppStorageException("Uploaded file can't be read.");
        }
    }

    /*    @Override
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
    }*/

    @Override
    public void update(long id, TaskUpdateDto taskUpdateDto) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no task with id = %d", id))
        );

        if (taskEntity.getStatus() == TaskStatus.DONE || taskEntity.getStatus() == TaskStatus.CANCELED) {
            throw new BoardAppIncorrectStateException("Task in DONE or CANCELED state  can't be modified");
        }

        String newName = taskUpdateDto.getName();
        if (newName != null) {
            if (newName.isEmpty()) {
                throw new IllegalArgumentException("Task name can't be an empty string");
            }
            taskEntity.setName(newName);
        }

        String newDescription = taskUpdateDto.getDescription();
        if (newDescription != null) {
            if (newDescription.isEmpty()) {
                throw new IllegalArgumentException("Task description can't be an empty string");
            }
            taskEntity.setName(newDescription);
        }

        TaskStatus newTaskStatus = taskUpdateDto.getStatus();
        if (newTaskStatus != null) {
            TaskStatus currentStatus = taskEntity.getStatus();
            if (currentStatus == TaskStatus.BACKLOG
                    && Set.of(TaskStatus.IN_PROGRESS, TaskStatus.CANCELED).contains(newTaskStatus)
 /*                   && newTaskStatus != TaskStatus.IN_PROGRESS
                    && newTaskStatus != TaskStatus.CANCELED*/) {
                throw new BoardAppIncorrectStateException("Task in BACKLOG status can be moved to IN_PROGRESS or CANCELED status only.");
            }

            if (currentStatus == TaskStatus.IN_PROGRESS
                    && Set.of(TaskStatus.CANCELED, TaskStatus.DONE).contains(newTaskStatus)
                   /* && newTaskStatus != TaskStatus.DONE
                    && newTaskStatus != TaskStatus.CANCELED*/) {
                throw new BoardAppIncorrectStateException("Task in IN_PROGRESS status can be moved to DONE or CANCELED status only.");
            }

            if (currentStatus == TaskStatus.BACKLOG
                    && newTaskStatus == TaskStatus.IN_PROGRESS
                    && taskUpdateDto.getExecutorId() == null) {
                throw new BoardAppIncorrectStateException("Task can't be moved to IN_PROGRESS without defining an executor");
            }

            if (Set.of(TaskStatus.CANCELED, TaskStatus.DONE).contains(newTaskStatus)
                    /*newTaskStatus == TaskStatus.CANCELED || newTaskStatus == TaskStatus.DONE*/) {
                taskEntity.setDoneOn(LocalDateTime.now());
            }

            taskEntity.setStatus(newTaskStatus);
        }

        Long newExecutorId = taskUpdateDto.getExecutorId();
        if (newExecutorId != null) {
            if (taskEntity.getStatus() != TaskStatus.IN_PROGRESS && taskEntity.getStatus() != TaskStatus.BACKLOG) {
                throw new BoardAppIncorrectStateException("Can't set an executor for task in a status other than IN_PROGRESS or BACKLOG.");
            }
            PersonEntity executor = personRepository.findById(newExecutorId).orElseThrow(
                    () -> new BoardAppIncorrectIdException(String.format("There is no person with id = %d", newExecutorId))
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
