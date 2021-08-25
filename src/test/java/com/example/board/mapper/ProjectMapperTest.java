package com.example.board.mapper;

import com.example.board.entity.person.PersonEntity;
import com.example.board.entity.project.ProjectEntity;
import com.example.board.entity.project.ProjectStatus;
import com.example.board.repository.PersonRepository;
import com.example.board.rest.dto.project.ProjectCreateDto;
import com.example.board.rest.dto.project.ProjectReadDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectMapperTest {

    @Mock
    PersonRepository personRepository;
    @InjectMocks
    ProjectMapperImpl projectMapper;

    long notExistingPersonId = 0L;
    PersonEntity person1 = new PersonEntity(); {person1.setId(1L);}
    PersonEntity person2 = new PersonEntity(); {person2.setId(2L);}

    @Test
    void projectCreateDtoToProjectEntity() {
        //Given
        ProjectCreateDto createDto = new ProjectCreateDto("New Project","New description", person1.getId());

        //When
        when(personRepository.findById(person1.getId())).thenReturn(Optional.of(person1));

        //Then
        ProjectEntity actualProjectEntity = projectMapper.projectCreateDtoToProjectEntity(createDto);
        assertNull(actualProjectEntity.getId());
        assertEquals(createDto.getName(), actualProjectEntity.getName());
        assertEquals(createDto.getDescription(), actualProjectEntity.getDescription());
        assertEquals(ProjectStatus.OPEN, actualProjectEntity.getStatus());
        assertEquals(person1, actualProjectEntity.getCustomer());
        assertNull(actualProjectEntity.getReleases());
        assertNull(actualProjectEntity.getCost());
    }

    @Test
    void projectEntityToProjectReadDto() {
        //Given
        ProjectEntity project = new ProjectEntity.Builder()
                .addId(1L)
                .addName("ProjectName")
                .addDescription("ProjectDescription")
                .addStatus(ProjectStatus.OPEN)
                .addCost(100L)
                .addCustomer(new PersonEntity(23L, null,null,null,null,null,null,null))
                .addReleases(null)
                .build();

        //When

        //Then
        ProjectReadDto actualReadDto = projectMapper.projectEntityToProjectReadDto(project);
        assertEquals(project.getId(),actualReadDto.getId());
        assertEquals(project.getName(), actualReadDto.getName());
        assertEquals(project.getDescription(), actualReadDto.getDescription());
        assertEquals(project.getStatus(), actualReadDto.getStatus());
        assertEquals(project.getCost(), actualReadDto.getCost());
        assertEquals(project.getCustomer().getId(), actualReadDto.getCustomerId());
    }

    @Test
    void customerIdToCustomer() {
        //Given


        //When
        when(personRepository.findById(notExistingPersonId)).thenReturn(Optional.empty());
        when(personRepository.findById(person1.getId())).thenReturn(Optional.of(person1));

        //Then
        assertThrows(BoardAppIncorrectIdException.class, () -> projectMapper.customerIdToCustomer(notExistingPersonId));
        assertEquals(person1, projectMapper.customerIdToCustomer(person1.getId()));

    }

    @Test
    void projectEntityListToProjectReadDtoList() {
        //Given

        ProjectEntity project1 = new ProjectEntity.Builder()
                .addId(1L)
                .addName("ProjectName1")
                .addDescription("ProjectDescription")
                .addStatus(ProjectStatus.OPEN)
                .addCost(100L)
                .addCustomer(new PersonEntity(23L, null,null,null,null,null,null,null))
                .addReleases(null)
                .build();

        ProjectEntity project2 = new ProjectEntity.Builder()
                .addId(1L)
                .addName("ProjectName2")
                .addDescription("ProjectDescription")
                .addStatus(ProjectStatus.OPEN)
                .addCost(100L)
                .addCustomer(new PersonEntity(23L, null,null,null,null,null,null,null))
                .addReleases(null)
                .build();

        //When

        //Then
        List<ProjectReadDto> actualDtoList = projectMapper.projectEntityListToProjectReadDtoList(List.of(project1, project2));
        assertEquals(2, actualDtoList.size());
        assertTrue(actualDtoList.stream()
                .map(ProjectReadDto::getName).collect(Collectors.toList())
                .containsAll(Set.of(project1.getName(), project2.getName())));

    }
}