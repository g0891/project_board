package com.example.board.service.implementation;

import com.example.board.entity.person.PersonEntity;
import com.example.board.entity.release.ReleaseEntity;
import com.example.board.entity.release.ReleaseStatus;
import com.example.board.entity.role.PersonRole;
import com.example.board.entity.role.RoleEntity;
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
import com.example.board.rest.errorController.exception.BoardAppIllegalArgumentException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectRoleException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectStateException;
import com.example.board.service.StorageService;
import com.example.board.service.specification.TaskSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private PersonMapper personMapper;
    @Mock
    private StorageService storageService;
    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void getById() {
        //Given
        long notExistingTaskId = 1L;
        long existingTaskId = 2L;
        TaskEntity taskEntity = new TaskEntity.Builder().addId(existingTaskId).build();
        TaskReadDto taskReadDto = new TaskReadDto(); taskReadDto.setId(existingTaskId);

        //When
        when(taskRepository.findById(notExistingTaskId)).thenReturn(Optional.empty());
        when(taskRepository.findById(existingTaskId)).thenReturn(Optional.of(taskEntity));
        when(taskMapper.taskEntityToTaskReadDto(taskEntity)).thenReturn(taskReadDto);

        //Then
        assertThrows(BoardAppIncorrectIdException.class, () -> taskService.getById(notExistingTaskId));
        assertEquals(taskReadDto, taskService.getById(existingTaskId));
    }

    @Test
    void getAll() {
        //Given
        List<TaskEntity> taskEntities = List.of(new TaskEntity(), new TaskEntity());
        List<TaskReadDto> taskReadDtoList = List.of(new TaskReadDto(), new TaskReadDto());

        //When
        when(taskRepository.findAll()).thenReturn(taskEntities);
        when(taskMapper.taskEntityListToTaskReadDtoList(taskEntities)).thenReturn(taskReadDtoList);

        //Then
        assertEquals(taskReadDtoList, taskService.getAll());
    }

    @Test
    void getFiltered() {
        //Given
        TaskEntity taskEntity1 = new TaskEntity.Builder().addStatus(TaskStatus.DONE).addName("superTask").build();
        TaskEntity taskEntity2 = new TaskEntity.Builder().addStatus(TaskStatus.DONE).addName("superTask2").build();
        List<TaskEntity> taskEntities = List.of(taskEntity1, taskEntity2);
        TaskSearchDto searchDto = new TaskSearchDto.Builder().addStatus(TaskStatus.DONE).build();
        Specification<TaskEntity> spec = TaskSpecification.get(searchDto);
        MockedStatic<TaskSpecification>  specMock = mockStatic(TaskSpecification.class);

        List<TaskReadDto> readDtos = List.of(
                new TaskReadDto(1L,taskEntity1.getName(),null,null,0L,null,0L,null,null),
                new TaskReadDto(2L,taskEntity2.getName(),null,null,0L,null,0L,null,null)
        );

        //When
        specMock.when(() -> TaskSpecification.get(searchDto)).thenReturn(spec);
        when(taskRepository.findAll(spec)).thenReturn(taskEntities);
        when(taskMapper.taskEntityListToTaskReadDtoList(taskEntities)).thenReturn(readDtos);


        //Then
        assertEquals(2, taskService.getFiltered(searchDto).size());
        assertTrue(taskService.getFiltered(searchDto).stream().allMatch(dto -> dto.getName().startsWith("superTask")));
    }

    @Test
    void add() {
        //Given
        TaskCreateDto taskForNotOpenedReleaseDto = new TaskCreateDto(null,null,0L,null,1L);
        TaskEntity taskForNotOpenedReleaseEntity =
                new TaskEntity.Builder().addRelease(
                        new ReleaseEntity.Builder().addStatus(ReleaseStatus.CLOSED).build()).build();
        TaskCreateDto taskWithIncorrectAuthorDto = new TaskCreateDto(null,null,2L,null,2L);
        Set<RoleEntity> withoutAuthorRolesEntitiesSet = Set.of(new RoleEntity(1, "CUSTOMER"));
        Set<PersonRole> withoutAuthorRolesPersonRoleSet = Set.of(PersonRole.CUSTOMER);
        PersonEntity personNotAuthor = new PersonEntity("John", withoutAuthorRolesEntitiesSet);
        TaskEntity taskWithIncorrectAuthorEntity =
                new TaskEntity.Builder().addAuthor(personNotAuthor).addRelease(
                    new ReleaseEntity.Builder().addStatus(ReleaseStatus.OPEN).build()).build();
        TaskCreateDto taskWithCorrectAuthorDto = new TaskCreateDto(null,null,3L,null,2L);
        Set<RoleEntity> withAuthorRolesEntitiesSet = Set.of(new RoleEntity(1, "AUTHOR"));
        Set<PersonRole> withAuthorRolesPersonRoleSet = Set.of(PersonRole.AUTHOR);
        PersonEntity personAuthor = new PersonEntity("John", withAuthorRolesEntitiesSet);
        TaskEntity taskWithCorrectAuthorEntity =
                new TaskEntity.Builder().addAuthor(personAuthor).addRelease(
                        new ReleaseEntity.Builder().addStatus(ReleaseStatus.OPEN).build()).build();
        TaskEntity taskWithCorrectAuthorEntitySaved =
                new TaskEntity.Builder().addId(25L).addAuthor(personAuthor).addRelease(
                        new ReleaseEntity.Builder().addStatus(ReleaseStatus.OPEN).build()).build();

        //When
        when(taskMapper.taskCreateDtoToTaskEntity(taskForNotOpenedReleaseDto)).thenReturn(taskForNotOpenedReleaseEntity);
        when(taskMapper.taskCreateDtoToTaskEntity(taskWithIncorrectAuthorDto)).thenReturn(taskWithIncorrectAuthorEntity);
        when(taskMapper.taskCreateDtoToTaskEntity(taskWithCorrectAuthorDto)).thenReturn(taskWithCorrectAuthorEntity);
        when(personMapper.roleEntitySetToPersonRoleSet(withoutAuthorRolesEntitiesSet)).thenReturn(withoutAuthorRolesPersonRoleSet);
        when(personMapper.roleEntitySetToPersonRoleSet(withAuthorRolesEntitiesSet)).thenReturn(withAuthorRolesPersonRoleSet);
        when(taskRepository.save(taskWithCorrectAuthorEntity)).thenReturn(taskWithCorrectAuthorEntitySaved);

        //Then
        assertThrows(BoardAppIncorrectStateException.class, () -> taskService.add(taskForNotOpenedReleaseDto));
        assertThrows(BoardAppIncorrectRoleException.class, () -> taskService.add(taskWithIncorrectAuthorDto));
        assertEquals(25L, taskService.add(taskWithCorrectAuthorDto));
    }

    @Test
    void update() {
        //Given
        long notExistingPersonId = 1L;
        PersonEntity executorPerson = new PersonEntity("John",Set.of(new RoleEntity(1,"EXECUTOR")));
        executorPerson.setId(2L);
        PersonEntity notExecutorPerson = new PersonEntity("Add",Set.of(new RoleEntity(2,"AUTHOR")));
        notExecutorPerson.setId(3L);
        Set<PersonRole> executorRightsSet = Set.of(PersonRole.EXECUTOR);
        Set<PersonRole> notExecutorRightsSet = Set.of(PersonRole.AUTHOR);
        long notExistingTaskId = 1L;
        TaskEntity doneTaskEntity = new TaskEntity.Builder().addId(2L).addStatus(TaskStatus.DONE).build();
        TaskEntity cancelledTaskEntity = new TaskEntity.Builder().addId(3L).addStatus(TaskStatus.CANCELED).build();
        long templateTaskId = 4L;
        TaskEntity templateTaskEntity = new TaskEntity();
        TaskUpdateDto badNameTaskUpdateDto = new TaskUpdateDto.Builder().addName("").build();
        TaskUpdateDto goodNameTaskUpdateDto = new TaskUpdateDto.Builder().addName("GoodNameTask").build();
        TaskUpdateDto badDescriptionTaskUpdateDto = new TaskUpdateDto.Builder().addDescription("").build();
        TaskUpdateDto goodDescriptionTaskUpdateDto = new TaskUpdateDto.Builder().addDescription("GoodDescriptionTask").build();
        TaskEntity backlogTaskWithoutExecutorTaskEntity = new TaskEntity.Builder().addId(5L).addStatus(TaskStatus.BACKLOG).build();
        TaskEntity backlogTaskWithExecutorTaskEntity = new TaskEntity.Builder().addId(6L).addStatus(TaskStatus.BACKLOG).addExecutor(new PersonEntity()).build();
        TaskUpdateDto toDoneStatusDto = new TaskUpdateDto.Builder().addStatus(TaskStatus.DONE).build();
        TaskUpdateDto toInProgressStatusDto = new TaskUpdateDto.Builder().addStatus(TaskStatus.IN_PROGRESS).build();
        TaskUpdateDto toInProgressStatusWithExecutorDto = new TaskUpdateDto.Builder().addStatus(TaskStatus.IN_PROGRESS).addExecutorId(executorPerson.getId()).build();
        TaskEntity inProgressTaskEntity = new TaskEntity.Builder().addId(7L).addStatus(TaskStatus.IN_PROGRESS).build();
        TaskUpdateDto toBacklogStatus = new TaskUpdateDto.Builder().addStatus(TaskStatus.BACKLOG).build();
        TaskUpdateDto setNotExistingPersonDto = new TaskUpdateDto.Builder().addExecutorId(notExistingPersonId).build();
        TaskUpdateDto setNotExecutorPersonDto = new TaskUpdateDto.Builder().addExecutorId(notExecutorPerson.getId()).build();
        TaskUpdateDto setExecutorPersonDto = new TaskUpdateDto.Builder().addExecutorId(executorPerson.getId()).build();


        //When
        when(taskRepository.findById(notExistingTaskId)).thenReturn(Optional.empty());
        when(taskRepository.findById(doneTaskEntity.getId())).thenReturn(Optional.of(doneTaskEntity));
        when(taskRepository.findById(cancelledTaskEntity.getId())).thenReturn(Optional.of(cancelledTaskEntity));
        when(taskRepository.findById(templateTaskId)).thenReturn(Optional.of(templateTaskEntity));
        when(taskRepository.findById(backlogTaskWithoutExecutorTaskEntity.getId())).thenReturn(Optional.of(backlogTaskWithoutExecutorTaskEntity));
        when(taskRepository.findById(backlogTaskWithExecutorTaskEntity.getId())).thenReturn(Optional.of(backlogTaskWithExecutorTaskEntity));
        when(personRepository.findById(notExistingPersonId)).thenReturn(Optional.empty());
        when(personRepository.findById(executorPerson.getId())).thenReturn(Optional.of(executorPerson));
        when(personRepository.findById(notExecutorPerson.getId())).thenReturn(Optional.of(notExecutorPerson));
        when(personMapper.roleEntitySetToPersonRoleSet(executorPerson.getRoles())).thenReturn(executorRightsSet);
        when(personMapper.roleEntitySetToPersonRoleSet(notExecutorPerson.getRoles())).thenReturn(notExecutorRightsSet);
        when(taskRepository.findById(inProgressTaskEntity.getId())).thenReturn(Optional.of(inProgressTaskEntity));

        when(taskRepository.save(any())).thenReturn(null);


        //Then
        assertThrows(BoardAppIncorrectIdException.class, () -> taskService.update(notExistingTaskId, any()));
        assertThrows(BoardAppIncorrectStateException.class, () -> taskService.update(doneTaskEntity.getId(), any()));
        assertThrows(BoardAppIncorrectStateException.class, () -> taskService.update(cancelledTaskEntity.getId(), any()));
        assertThrows(BoardAppIllegalArgumentException.class, () -> taskService.update(templateTaskId, badNameTaskUpdateDto));
        assertDoesNotThrow(() -> taskService.update(templateTaskId, goodNameTaskUpdateDto));
        assertThrows(BoardAppIllegalArgumentException.class, () -> taskService.update(templateTaskId, badDescriptionTaskUpdateDto));
        assertDoesNotThrow(() -> taskService.update(templateTaskId, goodDescriptionTaskUpdateDto));
        assertThrows(BoardAppIncorrectStateException.class, () -> taskService.update(backlogTaskWithoutExecutorTaskEntity.getId(), toDoneStatusDto));
        assertDoesNotThrow(() -> taskService.update(backlogTaskWithExecutorTaskEntity.getId(), toInProgressStatusDto));
        backlogTaskWithExecutorTaskEntity.setStatus(TaskStatus.BACKLOG);
        assertDoesNotThrow(() -> taskService.update(backlogTaskWithoutExecutorTaskEntity.getId(), toInProgressStatusWithExecutorDto));
        backlogTaskWithoutExecutorTaskEntity.setStatus(TaskStatus.BACKLOG); backlogTaskWithoutExecutorTaskEntity.setExecutor(null);
        assertThrows(BoardAppIncorrectStateException.class, () -> taskService.update(backlogTaskWithExecutorTaskEntity.getId(), toDoneStatusDto));
        assertThrows(BoardAppIncorrectStateException.class, () -> taskService.update(backlogTaskWithoutExecutorTaskEntity.getId(), toInProgressStatusDto));
        assertThrows(BoardAppIncorrectStateException.class, () -> taskService.update(inProgressTaskEntity.getId(), toBacklogStatus));
        assertThrows(BoardAppIncorrectIdException.class, () -> taskService.update(inProgressTaskEntity.getId(), setNotExistingPersonDto));
        assertThrows(BoardAppIncorrectRoleException.class, () -> taskService.update(inProgressTaskEntity.getId(), setNotExecutorPersonDto));
        assertDoesNotThrow(() -> taskService.update(inProgressTaskEntity.getId(), setExecutorPersonDto));

    }

    @Test
    void delete() {
        //Given
        long notExistingTaskId = 1L;
        long notBacklogTaskId= 2L;
        long backlogTaskId = 3L;
        TaskEntity notBacklogTaskEntity = new TaskEntity.Builder().addStatus(TaskStatus.IN_PROGRESS).build();
        TaskEntity backlogTaskEntity = new TaskEntity.Builder().addStatus(TaskStatus.BACKLOG).build();

        //When
        when(taskRepository.findById(notExistingTaskId)).thenThrow(BoardAppIncorrectIdException.class);
        when(taskRepository.findById(notBacklogTaskId)).thenReturn(Optional.of(notBacklogTaskEntity));
        when(taskRepository.findById(backlogTaskId)).thenReturn(Optional.of(backlogTaskEntity));
        doNothing().when(taskRepository).deleteById(any());

        //Then
        assertThrows(BoardAppIncorrectIdException.class, () -> taskService.delete(notExistingTaskId));
        assertThrows(BoardAppIncorrectStateException.class, () -> taskService.delete(notBacklogTaskId));
        assertDoesNotThrow(() -> taskService.delete(backlogTaskId));
        verify(taskRepository, times(1)).deleteById(any());
    }
}