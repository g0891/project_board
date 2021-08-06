package com.example.board.mapper;

import com.example.board.entity.PersonEntity;
import com.example.board.entity.ReleaseEntity;
import com.example.board.entity.TaskEntity;
import com.example.board.repository.PersonRepository;
import com.example.board.repository.ReleaseRepository;
import com.example.board.rest.dto.task.TaskCreateDto;
import com.example.board.rest.dto.task.TaskReadDto;
import com.example.board.rest.dto.task.TaskStatus;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", imports = {TaskStatus.class, LocalDateTime.class})
public abstract class TaskMapper {

    protected PersonRepository personRepository;
    protected ReleaseRepository releaseRepository;

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Autowired
    public void setReleaseRepository(ReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;
    }

    @Mappings({
            @Mapping(target = "authorId", expression = "java(taskEntity.getAuthor().getId())"),
            @Mapping(target = "executorId", expression = "java(taskEntity.getExecutor() == null ? null : taskEntity.getExecutor().getId())"),
            @Mapping(target = "releaseId", expression = "java(taskEntity.getRelease().getId())")
/*
            // id, name, description, status, createdOn, doneOn - mapped automatically
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "createdOn", source = "createdOn"),
            @Mapping(target = "doneOn", source = "doneOn")
*/
    })
    public abstract TaskReadDto taskEntityToTaskReadDto(TaskEntity taskEntity);

    @Mappings({
            @Mapping(target = "id", expression = "java(null)"),
            @Mapping(target = "status", expression = "java(TaskStatus.BACKLOG)"),
            @Mapping(target = "author", source = "authorId"),
            @Mapping(target = "executor", expression = "java(taskCreateDto.getExecutorId() == null ? null : personIdToPerson(taskCreateDto.getExecutorId()))"),
            @Mapping(target = "release", source = "releaseId"),
            @Mapping(target = "createdOn", expression = "java(LocalDateTime.now())"),
            @Mapping(target = "doneOn", expression = "java(null)")
    })
    public abstract TaskEntity taskCreateDtoToTaskEntity(TaskCreateDto taskCreateDto);

    public abstract List<TaskReadDto> taskEntityListToTaskReadDtoList(List<TaskEntity> taskEntities);

    public PersonEntity personIdToPerson(long id) throws BoardAppIncorrectIdException {
        return personRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no person with id = %d", id))
        );
    }

    public PersonEntity personIdToPerson(Long id) throws BoardAppIncorrectIdException {
        if (id == null) {
            return null;
        }
        return personRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no person with id = %d", id))
        );
    }

    public ReleaseEntity releaseIdToReleaseEntity(long id) throws BoardAppIncorrectIdException {
        return releaseRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no release with id = %d", id))
        );
    }
}
