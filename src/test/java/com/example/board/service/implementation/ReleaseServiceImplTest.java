package com.example.board.service.implementation;

import com.example.board.entity.project.ProjectEntity;
import com.example.board.entity.project.ProjectStatus;
import com.example.board.entity.release.ReleaseEntity;
import com.example.board.entity.release.ReleaseStatus;
import com.example.board.entity.task.TaskEntity;
import com.example.board.entity.task.TaskStatus;
import com.example.board.mapper.ReleaseMapper;
import com.example.board.repository.ProjectRepository;
import com.example.board.repository.ReleaseRepository;
import com.example.board.rest.dto.release.ReleaseCreateDto;
import com.example.board.rest.dto.release.ReleaseReadDto;
import com.example.board.rest.dto.release.ReleaseUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIllegalArgumentException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectStateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReleaseServiceImplTest {

    @Mock
    private ReleaseRepository releaseRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ReleaseMapper releaseMapper;
    @InjectMocks
    private ReleaseServiceImpl releaseService;

    @Test
    void getById() {
        //Given
        long notExistingReleaseId = 1L;
        long existingReleaseId = 2L;
        ReleaseEntity existingRelease = new ReleaseEntity.Builder().addId(existingReleaseId).build();
        ReleaseReadDto existingReleaseDto = new ReleaseReadDto();
        existingReleaseDto.setId(existingReleaseId);

        //When
        when(releaseRepository.findById(notExistingReleaseId)).thenReturn(Optional.empty());
        when(releaseRepository.findById(existingReleaseId)).thenReturn(Optional.of(existingRelease));
        when(releaseMapper.releaseEntityToReleaseReadDto(existingRelease)).thenReturn(existingReleaseDto);

        //Then
        assertEquals(existingReleaseDto, releaseService.getById(existingReleaseId));
        assertThrows(BoardAppIncorrectIdException.class, () -> releaseService.getById(notExistingReleaseId));

    }

    @Test
    void getAll() {
        //Given
        ReleaseEntity releaseEntity1 = new ReleaseEntity.Builder().addId(1L).addVersion("1.1").build();
        ReleaseEntity releaseEntity2 = new ReleaseEntity.Builder().addId(2L).addVersion("1.2").build();
        List<ReleaseEntity> releaseEntities = List.of(releaseEntity1, releaseEntity2);
        ReleaseReadDto releaseReadDto1 = new ReleaseReadDto(); releaseReadDto1.setId(1L); releaseReadDto1.setVersion("1.1");
        ReleaseReadDto releaseReadDto2 = new ReleaseReadDto(); releaseReadDto2.setId(2L); releaseReadDto2.setVersion("1.2");
        List<ReleaseReadDto> releaseReadDtos = List.of(releaseReadDto1, releaseReadDto2);

        //When
        when(releaseRepository.findAll()).thenReturn(releaseEntities);
        when(releaseMapper.releaseEntityListToReleaseReadDtoList(releaseEntities)).thenReturn(releaseReadDtos);

        //Then
        List<ReleaseReadDto> actualList = releaseService.getAll();
        assertEquals(2, actualList.size());
        assertTrue(releaseService.getAll().stream().map(ReleaseReadDto::getVersion).collect(Collectors.toSet()).containsAll(Set.of("1.1","1.2")));

    }

    @Test
    void add() {
        //Given
        long projectIdNotStarted = 1L;
        long projectIdStarted = 2L;
        ProjectEntity projectNotStarted = new ProjectEntity.Builder().addId(projectIdNotStarted).addStatus(ProjectStatus.OPEN).build();
        ProjectEntity projectStarted = new ProjectEntity.Builder().addId(projectIdStarted).addStatus(ProjectStatus.STARTED).build();
        ReleaseCreateDto releaseCreateNotStartedDto = new ReleaseCreateDto("1.0", projectIdNotStarted);
        ReleaseCreateDto releaseCreateStartedGoodDto = new ReleaseCreateDto("2.0", projectIdStarted);
        ReleaseCreateDto releaseCreateStartedEmptyVersionDto = new ReleaseCreateDto(" ", projectIdStarted);

        ReleaseEntity releaseEntityWithProjectNotStarted = new ReleaseEntity.Builder().addId(projectIdNotStarted).addProject(projectNotStarted).build();
        ReleaseEntity releaseEntityWithProjectStartedGood = new ReleaseEntity.Builder().addId(projectIdStarted).addVersion("2.0").addProject(projectStarted).build();
        ReleaseEntity releaseEntityWithProjectStartedGoodSaved = new ReleaseEntity.Builder().addId(projectIdStarted).addVersion("2.0").addProject(projectStarted).addId(99L).build();
        ReleaseEntity releaseEntityWithProjectStartedEmptyVersion = new ReleaseEntity.Builder().addId(projectIdStarted).addVersion(" ").addProject(projectStarted).build();

        //When
        when(releaseMapper.releaseCreateDtoToReleaseEntity(releaseCreateNotStartedDto)).thenReturn(releaseEntityWithProjectNotStarted);
        when(releaseMapper.releaseCreateDtoToReleaseEntity(releaseCreateStartedGoodDto)).thenReturn(releaseEntityWithProjectStartedGood);
        when(releaseMapper.releaseCreateDtoToReleaseEntity(releaseCreateStartedEmptyVersionDto)).thenReturn(releaseEntityWithProjectStartedEmptyVersion);
        when(releaseRepository.save(releaseEntityWithProjectStartedGood)).thenReturn(releaseEntityWithProjectStartedGoodSaved);

        //Then
        assertThrows(BoardAppIncorrectStateException.class, () -> releaseService.add(releaseCreateNotStartedDto));
        assertThrows(BoardAppIllegalArgumentException.class, () -> releaseService.add(releaseCreateStartedEmptyVersionDto));
        assertEquals(releaseEntityWithProjectStartedGoodSaved.getId(), releaseService.add(releaseCreateStartedGoodDto));
    }


    @Captor
    ArgumentCaptor<ReleaseEntity> releaseEntityArgumentCaptor;

    @Test
    void update() {
        //Given
        long notExistingReleaseId = 1L;
        long closedReleaseId = 2L;
        long notClosedReleaseId = 3L;

        ReleaseUpdateDto closeReleaseDto = new ReleaseUpdateDto(null, ReleaseStatus.CLOSED);
        ReleaseUpdateDto versionEmptyReleaseDto = new ReleaseUpdateDto(" ", null);
        ReleaseUpdateDto versionNotEmptyReleaseDto = new ReleaseUpdateDto("3.0", null);
        ReleaseEntity closedRelease = new ReleaseEntity.Builder().addStatus(ReleaseStatus.CLOSED).build();
        ReleaseEntity notClosedRelease = new ReleaseEntity.Builder().addStatus(ReleaseStatus.OPEN)
                .addTasks(List.of(
                        new TaskEntity.Builder().addStatus(TaskStatus.CANCELED).build(),
                        new TaskEntity.Builder().addStatus(TaskStatus.DONE).build(),
                        new TaskEntity.Builder().addStatus(TaskStatus.BACKLOG).build(),
                        new TaskEntity.Builder().addStatus(TaskStatus.IN_PROGRESS).build()
                )).build();

        //When
        when(releaseRepository.findById(notExistingReleaseId)).thenThrow(BoardAppIncorrectIdException.class);
        when(releaseRepository.findById(closedReleaseId)).thenReturn(Optional.of(closedRelease));
        when(releaseRepository.findById(notClosedReleaseId)).thenReturn((Optional.of(notClosedRelease)));
        when(releaseRepository.save(any())).thenReturn(null);

        //Then
        assertThrows(BoardAppIncorrectIdException.class, () -> releaseService.update(notExistingReleaseId, any()));
        assertThrows(BoardAppIncorrectStateException.class, () -> releaseService.update(closedReleaseId, any()));
        assertThrows(BoardAppIllegalArgumentException.class, () -> releaseService.update(notClosedReleaseId, versionEmptyReleaseDto));
        assertDoesNotThrow(() -> releaseService.update(notClosedReleaseId, versionNotEmptyReleaseDto));
        assertDoesNotThrow(() -> releaseService.update(notClosedReleaseId, closeReleaseDto));
        verify(releaseRepository, times(2)).save(releaseEntityArgumentCaptor.capture());
        assertEquals(4, releaseEntityArgumentCaptor.getAllValues().get(1).getTasks().size());
        assertTrue(releaseEntityArgumentCaptor.getAllValues().get(1).getTasks().stream().allMatch(task -> task.getStatus() == TaskStatus.CANCELED || task.getStatus() == TaskStatus.DONE));


    }

    @Test
    void delete() {
        //Given
        long notExistingReleaseId = 1L;
        long releaseWithTasksId = 2L;
        long releaseWithoutTasksId = 3L;
        ReleaseEntity releaseWithTasks = new ReleaseEntity.Builder().addId(releaseWithTasksId).addTasks(List.of(new TaskEntity())).build();
        ReleaseEntity releaseWithoutTasks = new ReleaseEntity.Builder().addId(releaseWithoutTasksId).addTasks(List.of()).build();

        //When
        when(releaseRepository.findById(notExistingReleaseId)).thenReturn(Optional.empty());
        when(releaseRepository.findById(releaseWithTasksId)).thenReturn(Optional.of(releaseWithTasks));
        when(releaseRepository.findById(releaseWithoutTasksId)).thenReturn(Optional.of(releaseWithoutTasks));
        doNothing().when(releaseRepository).deleteById(any());

        //Then
        assertThrows(BoardAppIncorrectIdException.class, () -> releaseService.delete(notExistingReleaseId));
        assertThrows(BoardAppIncorrectStateException.class, () -> releaseService.delete(releaseWithTasksId));
        releaseService.delete(releaseWithoutTasksId);
        verify(releaseRepository, times(1)).deleteById(any());

    }

    @Test
    void countCancelledForClosedRelease() {
        //Given
        long notExistingReleaseId = 1L;
        long notClosedReleaseId = 2L;
        long closedReleaseId = 3L;
        ReleaseEntity notClosedRelease = new ReleaseEntity.Builder().addId(notClosedReleaseId).addStatus(ReleaseStatus.OPEN).build();
        ReleaseEntity closedRelease = new ReleaseEntity.Builder().addId(closedReleaseId).addStatus(ReleaseStatus.CLOSED)
                .addTasks(List.of(
                        new TaskEntity.Builder().addStatus(TaskStatus.CANCELED).build(),
                        new TaskEntity.Builder().addStatus(TaskStatus.DONE).build(),
                        new TaskEntity.Builder().addStatus(TaskStatus.CANCELED).build()
                )).build();

        //When
        when(releaseRepository.findById(notExistingReleaseId)).thenThrow(BoardAppIncorrectIdException.class);
        when(releaseRepository.findById(notClosedReleaseId)).thenReturn(Optional.of(notClosedRelease));
        when(releaseRepository.findById(closedReleaseId)).thenReturn(Optional.of(closedRelease));

        //Then
        assertThrows(BoardAppIncorrectIdException.class, () -> releaseService.countCancelledForClosedRelease(notExistingReleaseId));
        assertThrows(BoardAppIncorrectStateException.class, () -> releaseService.countCancelledForClosedRelease(notClosedReleaseId));
        assertEquals(2, releaseService.countCancelledForClosedRelease(closedReleaseId));

    }
}