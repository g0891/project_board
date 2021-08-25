package com.example.board.mapper;

import com.example.board.entity.project.ProjectEntity;
import com.example.board.entity.release.ReleaseEntity;
import com.example.board.entity.release.ReleaseStatus;
import com.example.board.repository.ProjectRepository;
import com.example.board.rest.dto.release.ReleaseCreateDto;
import com.example.board.rest.dto.release.ReleaseReadDto;
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
class ReleaseMapperTest {

    @Mock
    ProjectRepository projectRepository;
    @InjectMocks
    ReleaseMapperImpl releaseMapper;

    @Test
    void releaseEntityToReleaseReadDto() {
        //Given
        ReleaseEntity release = new ReleaseEntity.Builder()
                .addId(1L)
                .addVersion("1.1")
                .addProject(new ProjectEntity.Builder().addId(57L).build())
                .addStatus(ReleaseStatus.CLOSED)
                .addCreatedOn(LocalDateTime.of(2021,8,25,13,17))
                .addReleasedOn(LocalDateTime.of(2021,8,25,17,31))
                .build();

        //When

        //Then
        ReleaseReadDto actualReleaseReadDto = releaseMapper.releaseEntityToReleaseReadDto(release);
        assertEquals(release.getId(), actualReleaseReadDto.getId());
        assertEquals(release.getVersion(), actualReleaseReadDto.getVersion());
        assertEquals(release.getProject().getId(), actualReleaseReadDto.getProjectId());
        assertEquals(release.getStatus(), actualReleaseReadDto.getStatus());
        assertEquals(release.getCreatedOn(), actualReleaseReadDto.getCreatedOn());
        assertEquals(release.getReleasedOn(), actualReleaseReadDto.getReleasedOn());
    }

    @Test
    void releaseCreateDtoToReleaseEntity() {
        //Given
        ProjectEntity project = new ProjectEntity.Builder().addId(1L).build();
        ReleaseCreateDto releaseCreateDto = new ReleaseCreateDto("1.2", project.getId());

        //When
        when(projectRepository.findById(project.getId())).thenReturn((Optional.of(project)));

        //Then
        ReleaseEntity actualReleaseEntity = releaseMapper.releaseCreateDtoToReleaseEntity(releaseCreateDto);
        assertNull(actualReleaseEntity.getId());
        assertEquals(releaseCreateDto.getVersion(), actualReleaseEntity.getVersion());
        assertEquals(releaseCreateDto.getProjectId(), actualReleaseEntity.getProject().getId());
        assertEquals(ReleaseStatus.OPEN, actualReleaseEntity.getStatus());
        assertNull(actualReleaseEntity.getReleasedOn());
    }

    @Test
    void projectIdToProjectEntity() {
        //Given
        long notExistingProjectId = 0L;
        ProjectEntity project = new ProjectEntity.Builder().addId(1L).build();

        //When
        when(projectRepository.findById(notExistingProjectId)).thenReturn(Optional.empty());
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

        //Then
        assertThrows(BoardAppIncorrectIdException.class, () -> releaseMapper.projectIdToProjectEntity(notExistingProjectId));
        assertEquals(project, releaseMapper.projectIdToProjectEntity(project.getId()));
    }

    @Test
    void releaseEntityListToReleaseReadDtoList() {
        //Given
        ReleaseEntity release1 = new ReleaseEntity.Builder()
                .addId(1L)
                .addVersion("1.1")
                .addProject(new ProjectEntity.Builder().addId(57L).build())
                .addStatus(ReleaseStatus.CLOSED)
                .addCreatedOn(LocalDateTime.of(2021,8,25,13,17))
                .addReleasedOn(LocalDateTime.of(2021,8,25,17,31))
                .build();
        ReleaseEntity release2 = new ReleaseEntity.Builder()
                .addId(2L)
                .addVersion("1.2")
                .addProject(new ProjectEntity.Builder().addId(57L).build())
                .addStatus(ReleaseStatus.CLOSED)
                .addCreatedOn(LocalDateTime.of(2021,8,25,13,17))
                .addReleasedOn(LocalDateTime.of(2021,8,25,17,31))
                .build();
        List<ReleaseEntity> releaseEntities = List.of(release1, release2);

        //When

        //Then
        List<ReleaseReadDto> releaseReadDtos = releaseMapper.releaseEntityListToReleaseReadDtoList(releaseEntities);
        assertEquals(2, releaseReadDtos.size());
        assertTrue(releaseReadDtos.stream()
                .map(ReleaseReadDto::getVersion).collect(Collectors.toSet()).
                containsAll(Set.of(release1.getVersion(), release2.getVersion())));
    }
}