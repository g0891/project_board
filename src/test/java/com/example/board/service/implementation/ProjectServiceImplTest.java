package com.example.board.service.implementation;

import com.example.board.entity.person.PersonEntity;
import com.example.board.entity.person.PersonStatus;
import com.example.board.entity.project.ProjectEntity;
import com.example.board.entity.project.ProjectStatus;
import com.example.board.entity.release.ReleaseEntity;
import com.example.board.entity.release.ReleaseStatus;
import com.example.board.entity.role.PersonRole;
import com.example.board.entity.role.RoleEntity;
import com.example.board.feign.BankingService;
import com.example.board.mapper.PersonMapper;
import com.example.board.mapper.ProjectMapper;
import com.example.board.repository.PersonRepository;
import com.example.board.repository.ProjectRepository;
import com.example.board.rest.dto.project.ProjectCreateDto;
import com.example.board.rest.dto.project.ProjectReadDto;
import com.example.board.rest.dto.project.ProjectUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIllegalArgumentException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectRoleException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectStateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private ProjectMapper projectMapper;
    @Mock
    private PersonMapper personMapper;
    @Mock
    private BankingService bankingService;
    @InjectMocks ProjectServiceImpl projectService;

    private final ProjectEntity projectEntity1 = new ProjectEntity("ProjectName", "ProjectDescr", ProjectStatus.OPEN, 13L, new PersonEntity("John", null, PersonStatus.ACTIVE));
    private final ProjectEntity projectEntity2 = new ProjectEntity("ProjectName2", "ProjectDescr2", ProjectStatus.CLOSED, 14L, new PersonEntity("Ann", null, PersonStatus.ACTIVE));
    private final ProjectReadDto projectReadDto1 = new ProjectReadDto(1L,"ProjectName","ProjectDescr", 1L, ProjectStatus.OPEN, 13L);
    private final ProjectReadDto projectReadDto2 = new ProjectReadDto(2L,"ProjectName2","ProjectDescr2", 2L, ProjectStatus.CLOSED, 14L);

    @Test
    void getById() {
        //Given
        long existingProjectId = 1L;
        long notExistingProjectId = 2L;

        //When
        when(projectRepository.findById(existingProjectId)).thenReturn(Optional.of(projectEntity1));
        when(projectRepository.findById(notExistingProjectId)).thenThrow(BoardAppIncorrectIdException.class);
        when(projectMapper.projectEntityToProjectReadDto(projectEntity1)).thenReturn(projectReadDto1);
        //Then
        assertEquals(projectReadDto1, projectService.getById(existingProjectId));
        assertThrows(BoardAppIncorrectIdException.class, () -> projectService.getById(notExistingProjectId));
    }

    @Test
    void getAll() {
        //Given
        List<ProjectEntity> projectEntities = List.of(projectEntity1, projectEntity2);
        List<ProjectReadDto> projectReadDtos = List.of(projectReadDto1, projectReadDto2);

        //When
        when(projectRepository.findAll()).thenReturn(projectEntities);
        when(projectMapper.projectEntityListToProjectReadDtoList(projectEntities)).thenReturn(projectReadDtos);

        //Then
        List<ProjectReadDto> actualDtoList = projectService.getAll();
        assertEquals(2, actualDtoList.size());
        assertTrue(actualDtoList.stream().map(ProjectReadDto::getName).collect(Collectors.toSet()).containsAll(Set.of("ProjectName", "ProjectName2")));
    }

    @Test
    void add() {
        //Given
        ProjectCreateDto projectCreateDto1 = new ProjectCreateDto("New project", "Some description", 55L);
        ProjectCreateDto projectCreateDto2 = new ProjectCreateDto("New project2", "Some description2", 55L);
        PersonEntity personEntity1 = new PersonEntity(
                projectCreateDto1.getCustomerId(),
                "John",
                null,
                PersonStatus.ACTIVE,
                Set.of(new RoleEntity(1, "CUSTOMER")),
                null,null,null
        );
        PersonEntity personEntity2 = new PersonEntity(
                projectCreateDto2.getCustomerId(),
                "Ann",
                null,
                PersonStatus.ACTIVE,
                Set.of(new RoleEntity(2, "AUTHOR")),
                null,null,null
        );
        ProjectEntity projectEntity1 = new ProjectEntity(projectCreateDto1.getName(),projectCreateDto1.getDescription(),ProjectStatus.OPEN,0L, personEntity1);
        ProjectEntity projectEntity2 = new ProjectEntity(projectCreateDto2.getName(),projectCreateDto2.getDescription(),ProjectStatus.OPEN,0L, personEntity2);
        ProjectEntity projectEntity1saved = new ProjectEntity(projectCreateDto1.getName(),projectCreateDto1.getDescription(),ProjectStatus.OPEN,0L, personEntity1);
        projectEntity1saved.setId(22L);

        //When
        when(projectMapper.projectCreateDtoToProjectEntity(projectCreateDto1)).thenReturn(projectEntity1);
        when(projectMapper.projectCreateDtoToProjectEntity(projectCreateDto2)).thenReturn(projectEntity2);
        when(personMapper.roleEntitySetToPersonRoleSet(projectEntity1.getCustomer().getRoles())).thenReturn(Set.of(PersonRole.CUSTOMER));
        when(personMapper.roleEntitySetToPersonRoleSet(projectEntity2.getCustomer().getRoles())).thenReturn(Set.of(PersonRole.AUTHOR));
        when(projectRepository.save(projectEntity1)).thenReturn(projectEntity1saved);

        //Then
        assertEquals(22L, projectService.add(projectCreateDto1));
        assertThrows(BoardAppIncorrectRoleException.class, () -> projectService.add(projectCreateDto2));
    }

    @Test
    void update() {
        //Given
        long notExistingProjectId = 1L;

        long closedProjectId = 2L;
        ProjectEntity closedProject = new ProjectEntity.Builder()
                .addId(closedProjectId)
                .addStatus(ProjectStatus.CLOSED)
                .build();

        long startedProjectId = 3L;
        ProjectEntity startedProject = new ProjectEntity.Builder()
                .addId(startedProjectId)
                .addStatus(ProjectStatus.STARTED)
                .build();

        long openProjectWithNoCostId = 4L;
        ProjectEntity openProjectWithNoCoast = new ProjectEntity.Builder()
                .addId(openProjectWithNoCostId)
                .addStatus(ProjectStatus.OPEN)
                .build();

        long openProjectWithCostPayedId = 51L;
        ProjectEntity openProjectWithCoastPayed = new ProjectEntity.Builder()
                .addId(openProjectWithCostPayedId)
                .addStatus(ProjectStatus.OPEN)
                .addCost(500L)
                .build();
        long openProjectWithCostNotPayedId = 52L;
        ProjectEntity openProjectWithCoastNotPayed = new ProjectEntity.Builder()
                .addId(openProjectWithCostNotPayedId)
                .addStatus(ProjectStatus.OPEN)
                .addCost(500L)
                .build();

        long startedProjectWithNotClosedReleasesId = 6L;
        ProjectEntity startedProjectWithNotClosedReleases = new ProjectEntity.Builder()
                .addId(startedProjectWithNotClosedReleasesId)
                .addStatus(ProjectStatus.STARTED)
                .addReleases(List.of(new ReleaseEntity(null,ReleaseStatus.OPEN,null,null,null)))
                .build();

        long startedProjectWithAllClosedReleasesId = 7L;
        ProjectEntity startedProjectWithAllClosedReleases = new ProjectEntity.Builder()
                .addId(startedProjectWithAllClosedReleasesId)
                .addStatus(ProjectStatus.STARTED)
                .addReleases(List.of(new ReleaseEntity(null,ReleaseStatus.CLOSED,null,null,null)))
                .build();

        ProjectUpdateDto updateCostDto = new ProjectUpdateDto.Builder().addCost(100L).build();
        ProjectUpdateDto updateCostNegativeDto = new ProjectUpdateDto.Builder().addCost(-100L).build();
        ProjectUpdateDto closeProjectDto = new ProjectUpdateDto.Builder().addStatus(ProjectStatus.CLOSED).build();
        ProjectUpdateDto openProjectDto = new ProjectUpdateDto.Builder().addStatus(ProjectStatus.OPEN).build();
        ProjectUpdateDto startProjectDto = new ProjectUpdateDto.Builder().addStatus(ProjectStatus.STARTED).build();
        ProjectUpdateDto updateNameToEmptyDto = new ProjectUpdateDto.Builder().addName("").build();
        ProjectUpdateDto updateNameToNotEmptyDto = new ProjectUpdateDto.Builder().addName("New Name").build();
        ProjectUpdateDto updateDescriptionToEmptyDto = new ProjectUpdateDto.Builder().addDescription("").build();
        ProjectUpdateDto updateDescriptionToNotEmptyDto = new ProjectUpdateDto.Builder().addDescription("New Description").build();

        long personWithNotExistingId = 1L;
        long personWithoutCustomerRoleId = 2L;
        long personWithCustomerRoleId = 3L;
        ProjectUpdateDto updateCustomerWithNotExistingIdDto = new ProjectUpdateDto.Builder().addCustomerId(personWithNotExistingId).build();
        ProjectUpdateDto updateCustomerWithoutCustomerRoleDto = new ProjectUpdateDto.Builder().addCustomerId(personWithoutCustomerRoleId).build();
        PersonEntity personEntityWithoutCustomerRole = new PersonEntity("Person1", Set.of(new RoleEntity(1, "AUTHOR")));
        ProjectUpdateDto updateCustomerWithCustomerRoleDto = new ProjectUpdateDto.Builder().addCustomerId(personWithCustomerRoleId).build();
        PersonEntity personEntityWithCustomerRole = new PersonEntity("Person2", Set.of(new RoleEntity(2, "CUSTOMER")));

        //When
        when(projectRepository.findById(notExistingProjectId)).thenThrow(BoardAppIncorrectIdException.class);
        when(projectRepository.findById(closedProjectId)).thenReturn(Optional.of(closedProject));
        when(projectRepository.findById(startedProjectId)).thenReturn(Optional.of(startedProject));
        when(projectRepository.findById(openProjectWithNoCostId)).thenReturn(Optional.of(openProjectWithNoCoast));
        when(projectRepository.findById(openProjectWithCostPayedId)).thenReturn(Optional.of(openProjectWithCoastPayed));
        when(projectRepository.findById(openProjectWithCostNotPayedId)).thenReturn(Optional.of(openProjectWithCoastNotPayed));
        when(bankingService.getTotalPaymentsForProject(openProjectWithCostPayedId)).thenReturn(ResponseEntity.ok(openProjectWithCoastPayed.getCost()));
        when(bankingService.getTotalPaymentsForProject(openProjectWithCostNotPayedId)).thenReturn(ResponseEntity.ok(0L));
        when(projectRepository.findById(startedProjectWithNotClosedReleasesId)).thenReturn(Optional.of(startedProjectWithNotClosedReleases));
        when(projectRepository.findById(startedProjectWithAllClosedReleasesId)).thenReturn(Optional.of(startedProjectWithAllClosedReleases));
        when(personRepository.findById(personWithNotExistingId)).thenReturn(Optional.empty());
        when(personRepository.findById(personWithoutCustomerRoleId)).thenReturn(Optional.of(personEntityWithoutCustomerRole));
        when(personRepository.findById(personWithCustomerRoleId)).thenReturn(Optional.of(personEntityWithCustomerRole));
        when(personMapper.roleEntitySetToPersonRoleSet(personEntityWithoutCustomerRole.getRoles())).thenReturn(Set.of(PersonRole.AUTHOR));
        when(personMapper.roleEntitySetToPersonRoleSet(personEntityWithCustomerRole.getRoles())).thenReturn(Set.of(PersonRole.CUSTOMER));
        when(projectRepository.save(any())).thenReturn(null);

        //Then
        assertThrows(BoardAppIncorrectIdException.class, () -> projectService.update(notExistingProjectId,null));
        assertThrows(BoardAppIncorrectStateException.class, () -> projectService.update(closedProjectId,null));
        assertThrows(BoardAppIncorrectStateException.class, () -> projectService.update(startedProjectId,updateCostDto));
        assertThrows(BoardAppIllegalArgumentException.class, () -> projectService.update(openProjectWithNoCostId, updateCostNegativeDto));
        assertThrows(BoardAppIncorrectStateException.class, () -> projectService.update(startedProjectWithNotClosedReleasesId,closeProjectDto));
        assertDoesNotThrow(() -> projectService.update(startedProjectWithAllClosedReleasesId,closeProjectDto));
        assertThrows(BoardAppIncorrectStateException.class, () -> projectService.update(startedProjectId,openProjectDto));
        assertThrows(BoardAppIncorrectStateException.class, () -> projectService.update(openProjectWithNoCostId, startProjectDto));
        assertThrows(BoardAppIncorrectStateException.class, () -> projectService.update(openProjectWithCostNotPayedId, startProjectDto));
        assertDoesNotThrow(() -> projectService.update(openProjectWithCostPayedId,startProjectDto));
        assertThrows(BoardAppIllegalArgumentException.class, () -> projectService.update(openProjectWithNoCostId,updateNameToEmptyDto));
        assertDoesNotThrow(() -> projectService.update(openProjectWithNoCostId,updateNameToNotEmptyDto));
        assertThrows(BoardAppIllegalArgumentException.class, () -> projectService.update(openProjectWithNoCostId,updateDescriptionToEmptyDto));
        assertDoesNotThrow(() -> projectService.update(openProjectWithNoCostId,updateDescriptionToNotEmptyDto));
        assertThrows(BoardAppIncorrectIdException.class, () -> projectService.update(openProjectWithNoCostId, updateCustomerWithNotExistingIdDto));
        assertThrows(BoardAppIncorrectRoleException.class, () -> projectService.update(openProjectWithNoCostId, updateCustomerWithoutCustomerRoleDto));
        assertDoesNotThrow(() -> projectService.update(openProjectWithNoCostId,updateCustomerWithCustomerRoleDto));
        verify(projectRepository, times(5)).save(any());

    }

    @Test
    void delete() {
        //Given
        long goodToDeleteId = 1L;
        long notExistingId = 2L;
        long withReleasesInside = 3L;

        ProjectEntity projectEntity1 = new ProjectEntity("Project1", "Descr1", ProjectStatus.OPEN, 0L, null);
        projectEntity1.setReleases(List.of());
        ProjectEntity projectEntity2 = new ProjectEntity("Project2", "Descr2", ProjectStatus.OPEN, 0L, null);
        projectEntity2.setReleases(List.of(new ReleaseEntity("1.0.1", ReleaseStatus.OPEN, null, null,projectEntity2)));

        //When
        when(projectRepository.findById(goodToDeleteId)).thenReturn(Optional.of(projectEntity1));
        when(projectRepository.findById(notExistingId)).thenReturn(Optional.empty());
        when(projectRepository.findById(withReleasesInside)).thenReturn(Optional.of(projectEntity2));
        doNothing().when(projectRepository).deleteById(any());

        //Then
        assertThrows(BoardAppIncorrectIdException.class, () -> projectService.delete(notExistingId));
        assertThrows(BoardAppIncorrectStateException.class, () -> projectService.delete(withReleasesInside));
        projectService.delete(goodToDeleteId);
        verify(projectRepository, times(1)).deleteById(goodToDeleteId);
    }
}