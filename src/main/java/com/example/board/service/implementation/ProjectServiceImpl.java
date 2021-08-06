package com.example.board.service.implementation;

import com.example.board.entity.person.PersonEntity;
import com.example.board.entity.project.ProjectEntity;
import com.example.board.mapper.PersonMapper;
import com.example.board.mapper.ProjectMapper;
import com.example.board.repository.PersonRepository;
import com.example.board.repository.ProjectRepository;
import com.example.board.entity.role.PersonRole;
import com.example.board.rest.dto.project.ProjectCreateDto;
import com.example.board.rest.dto.project.ProjectReadDto;
import com.example.board.entity.project.ProjectStatus;
//import com.example.board.rest.dto.project.ProjectUpdateDto;
import com.example.board.entity.release.ReleaseStatus;
import com.example.board.rest.dto.project.ProjectUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectRoleException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectStateException;
import com.example.board.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final PersonRepository personRepository;
    private final ProjectMapper projectMapper;
    private final PersonMapper personMapper;

    //@Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, PersonRepository personRepository, ProjectMapper projectMapper, PersonMapper personMapper) {
        this.projectRepository = projectRepository;
        this.personRepository = personRepository;
        this.projectMapper = projectMapper;
        this.personMapper = personMapper;
    }

    @Override
    public ProjectReadDto getById(long id) throws BoardAppIncorrectIdException {
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no project with id = %d", id)));
        return projectMapper.projectEntityToProjectReadDto(projectEntity);
    }

    @Override
    public List<ProjectReadDto> getAll() {
        List<ProjectEntity> projectEntities = projectRepository.findAll();
        return projectMapper.projectEntityListToProjectReadDtoList(projectEntities);
    }

    @Override
    public long add(ProjectCreateDto project) throws BoardAppIncorrectIdException {
        ProjectEntity projectEntity = projectMapper.projectCreateDtoToProjectEntity(project);

        if (!personMapper.roleEntitySetToPersonRoleSet(projectEntity.getCustomer().getRoles()).contains(PersonRole.CUSTOMER)) {
            throw new BoardAppIncorrectRoleException("Only person with CUSTOMER role can be a customer of the project.");
        }

        projectEntity = projectRepository.save(projectEntity);
        return projectEntity.getId();
    }

    /*@Override
    public void update(long id,
                       Optional<String> newName,
                       Optional<String> newDescription,
                       Optional<Long> newCustomerId,
                       Optional<ProjectStatus> newStatus) {

        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no project with id = %d", id))
        );

        if (projectEntity.getStatus() == ProjectStatus.CLOSED) {
            throw new BoardAppIncorrectStateException("Can't change an already CLOSED project.");
        }

        if (newStatus.isPresent() && (newStatus.get() == ProjectStatus.CLOSED)) {
            if (projectEntity.getReleases().stream().anyMatch(release -> release.getStatus() != ReleaseStatus.CLOSED)) {
                throw new BoardAppIncorrectStateException("Can't close the project containing a release in a not CLOSED state.");
            }

            projectEntity.setStatus(newStatus.get());
        }

        newName.ifPresent(projectEntity::setName);
        newDescription.ifPresent(projectEntity::setDescription);

        if (newCustomerId.isPresent()) {
            PersonEntity personEntity = personRepository.findById(newCustomerId.get()).orElseThrow(
                    () -> new BoardAppIncorrectIdException(String.format("There is no customer with id = %d", newCustomerId.get()))
            );

            if (!personMapper.roleEntitySetToPersonRoleSet(personEntity.getRoles()).contains(PersonRole.CUSTOMER)) {
                throw new BoardAppIncorrectRoleException("A person can't be set as a customer for a project because the CUSTOMER role is missing.");
            }

            projectEntity.setCustomer(personEntity);
        }

        projectRepository.save(projectEntity);

    }*/

    @Override
    public void update(long id, ProjectUpdateDto projectUpdateDto) {
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no project with id = %d", id))
        );

        if (projectEntity.getStatus() == ProjectStatus.CLOSED) {
            throw new BoardAppIncorrectStateException("Can't change an already CLOSED project.");
        }

        ProjectStatus newStatus = projectUpdateDto.getStatus();
        if (newStatus == ProjectStatus.CLOSED) {
            if (projectEntity.getReleases().stream().anyMatch(release -> release.getStatus() != ReleaseStatus.CLOSED)) {
                throw new BoardAppIncorrectStateException("Can't close the project containing a release in a not CLOSED state.");
            }
            projectEntity.setStatus(newStatus);
        }

        String newName = projectUpdateDto.getName();
        if (newName != null) {
            if (newName.isEmpty()) {
                throw new IllegalArgumentException("Project name can't be an empty string");
            }
            projectEntity.setName(newName);
        }

        String newDescription = projectUpdateDto.getDescription();
        if (newDescription != null) {
            if (newDescription.isEmpty()) {
                throw new IllegalArgumentException("Project description can't be an empty string");
            }
            projectEntity.setDescription(newDescription);
        }

        Long newCustomerId = projectUpdateDto.getCustomerId();
        if (newCustomerId != null) {
            PersonEntity personEntity = personRepository.findById(newCustomerId).orElseThrow(
                    () -> new BoardAppIncorrectIdException(String.format("There is no customer with id = %d", newCustomerId))
            );

            if (!personMapper.roleEntitySetToPersonRoleSet(personEntity.getRoles()).contains(PersonRole.CUSTOMER)) {
                throw new BoardAppIncorrectRoleException("A person can't be set as a customer for a project because the CUSTOMER role is missing.");
            }

            projectEntity.setCustomer(personEntity);
        }

        projectRepository.save(projectEntity);
    }

    @Override
    public void delete(long id) throws BoardAppIncorrectIdException {
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no project with id = %d", id))
        );

        if (!projectEntity.getReleases().isEmpty()) {
            throw new BoardAppIncorrectStateException("Can't delete project with releases inside.");
        }

        projectRepository.deleteById(id);
    }
}
