package com.example.board.mapper;

import com.example.board.entity.person.PersonEntity;
import com.example.board.entity.release.ReleaseEntity;
import com.example.board.entity.task.TaskEntity;
import com.example.board.entity.task.TaskStatus;
import com.example.board.repository.PersonRepository;
import com.example.board.repository.ReleaseRepository;
import com.example.board.rest.dto.task.TaskCreateDto;
import com.example.board.rest.dto.task.TaskReadDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskMapperTest {
    @Mock
    PersonRepository personRepository;
    @Mock
    ReleaseRepository releaseRepository;
    @InjectMocks
    TaskMapperImpl taskMapper;

    @Test
    void taskEntityToTaskReadDto() {
        //Given
        TaskEntity taskEntity = new TaskEntity.Builder()
                .addId(1L)
                .addName("Task Name")
                .addDescription("Task Description")
                .addStatus(TaskStatus.DONE)
                .addCreatedOn(LocalDateTime.of(2021,8,25,13,7))
                .addDoneOn(LocalDateTime.of(2021,8,25,17,31))
                .addAuthor(new PersonEntity(31L, null,null,null,null,null,null,null))
                .addExecutor(new PersonEntity(32L, null,null,null,null,null,null,null))
                .addRelease(new ReleaseEntity.Builder().addId(100L).build())
                .build();

        //When

        //Then
        TaskReadDto actualTaskReadDto = taskMapper.taskEntityToTaskReadDto(taskEntity);
        assertEquals(taskEntity.getId(), actualTaskReadDto.getId());
        assertEquals(taskEntity.getName(), actualTaskReadDto.getName());
        assertEquals(taskEntity.getDescription(), actualTaskReadDto.getDescription());
        assertEquals(taskEntity.getStatus(), actualTaskReadDto.getStatus());
        assertEquals(taskEntity.getCreatedOn(), actualTaskReadDto.getCreatedOn());
        assertEquals(taskEntity.getDoneOn(), actualTaskReadDto.getDoneOn());
        assertEquals(taskEntity.getAuthor().getId(), actualTaskReadDto.getAuthorId());
        assertEquals(taskEntity.getExecutor().getId(), actualTaskReadDto.getExecutorId());
        assertEquals(taskEntity.getRelease().getId(), actualTaskReadDto.getReleaseId());

    }

    @Test
    void taskCreateDtoToTaskEntity() {
        //Given
        PersonEntity author = new PersonEntity(); author.setId(1L);
        PersonEntity executor = new PersonEntity(); executor.setId(2L);
        ReleaseEntity releaseEntity = new ReleaseEntity.Builder().addId(100L).build();
        TaskCreateDto taskCreateDto = new TaskCreateDto("New task","New description", author.getId(), executor.getId(), releaseEntity.getId());

        //When
        when(personRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(personRepository.findById(executor.getId())).thenReturn(Optional.of(executor));
        when(releaseRepository.findById(releaseEntity.getId())).thenReturn(Optional.of(releaseEntity));

        //Then
        TaskEntity actualTaskEntity = taskMapper.taskCreateDtoToTaskEntity(taskCreateDto);
        assertNull(actualTaskEntity.getId());
        assertEquals(taskCreateDto.getName(), actualTaskEntity.getName());
        assertEquals(taskCreateDto.getDescription(), actualTaskEntity.getDescription());
        assertEquals(TaskStatus.BACKLOG, actualTaskEntity.getStatus());
        assertEquals(taskCreateDto.getAuthorId(), actualTaskEntity.getAuthor().getId());
        assertEquals(taskCreateDto.getExecutorId(), actualTaskEntity.getExecutor().getId());
        assertEquals(taskCreateDto.getReleaseId(), actualTaskEntity.getRelease().getId());
        assertNotNull(actualTaskEntity.getCreatedOn());
        assertNull(actualTaskEntity.getDoneOn());
    }

    @Test
    void taskEntityListToTaskReadDtoList() {
        //Given
        TaskEntity taskEntity1 = new TaskEntity.Builder()
                .addId(1L)
                .addName("Task Name1")
                .addDescription("Task Description")
                .addStatus(TaskStatus.DONE)
                .addCreatedOn(LocalDateTime.of(2021,8,25,13,7))
                .addDoneOn(LocalDateTime.of(2021,8,25,17,31))
                .addAuthor(new PersonEntity(31L, null,null,null,null,null,null,null))
                .addExecutor(new PersonEntity(32L, null,null,null,null,null,null,null))
                .addRelease(new ReleaseEntity.Builder().addId(100L).build())
                .build();
        //Given
        TaskEntity taskEntity2 = new TaskEntity.Builder()
                .addId(2L)
                .addName("Task Name2")
                .addDescription("Task Description")
                .addStatus(TaskStatus.DONE)
                .addCreatedOn(LocalDateTime.of(2021,8,25,13,7))
                .addDoneOn(LocalDateTime.of(2021,8,25,17,31))
                .addAuthor(new PersonEntity(31L, null,null,null,null,null,null,null))
                .addExecutor(new PersonEntity(32L, null,null,null,null,null,null,null))
                .addRelease(new ReleaseEntity.Builder().addId(100L).build())
                .build();
        List<TaskEntity> taskEntities = List.of(taskEntity1, taskEntity2);

        //When

        //Then
        List<TaskReadDto> actualTaskReadDtoList = taskMapper.taskEntityListToTaskReadDtoList(taskEntities);
        assertEquals(2, actualTaskReadDtoList.size());
        assertTrue(actualTaskReadDtoList.stream().map(TaskReadDto::getName).collect(Collectors.toSet())
                    .containsAll(Set.of(taskEntity1.getName(), taskEntity2.getName())));

    }

    @Test
    void personIdNotNullToPerson() {
        //Given
        long notExistingPerson = 0L;
        PersonEntity personEntity = new PersonEntity(); personEntity.setId(1L);

        //When
        when(personRepository.findById(notExistingPerson)).thenReturn(Optional.empty());
        when(personRepository.findById(personEntity.getId())).thenReturn(Optional.of(personEntity));

        //Then
        assertThrows(BoardAppIncorrectIdException.class, () -> taskMapper.personIdToPerson(notExistingPerson));
        assertEquals(personEntity, taskMapper.personIdToPerson(personEntity.getId().longValue()));
    }

    @Test
    void testPersonIdNullableToPerson() {
        //Given
        Long notExistingPerson = 0L;
        PersonEntity personEntity = new PersonEntity(); personEntity.setId(1L);

        //When
        when(personRepository.findById(notExistingPerson)).thenReturn(Optional.empty());
        when(personRepository.findById(personEntity.getId())).thenReturn(Optional.of(personEntity));

        //Then
        assertNull(taskMapper.personIdToPerson(null));
        assertThrows(BoardAppIncorrectIdException.class, () -> taskMapper.personIdToPerson(notExistingPerson));
        assertEquals(personEntity, taskMapper.personIdToPerson(personEntity.getId()));
    }

    @Test
    void releaseIdToReleaseEntity() {
        //Given
        long notExistingReleaseId = 0L;
        ReleaseEntity releaseEntity = new ReleaseEntity.Builder().addId(1L).build();

        //When
        when(releaseRepository.findById(notExistingReleaseId)).thenReturn(Optional.empty());
        when(releaseRepository.findById(releaseEntity.getId())).thenReturn(Optional.of(releaseEntity));

        //Then
        assertThrows(BoardAppIncorrectIdException.class, () -> taskMapper.releaseIdToReleaseEntity(notExistingReleaseId));
        assertEquals(releaseEntity, taskMapper.releaseIdToReleaseEntity(releaseEntity.getId()));
    }
}