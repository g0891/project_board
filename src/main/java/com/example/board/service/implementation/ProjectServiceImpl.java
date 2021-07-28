package com.example.board.service.implementation;

import com.example.board.entity.PersonEntity;
import com.example.board.entity.ProjectEntity;
import com.example.board.mapper.ProjectMapper;
import com.example.board.repository.PersonRepository;
import com.example.board.repository.ProjectRepository;
import com.example.board.rest.dto.project.ProjectCreateDto;
import com.example.board.rest.dto.project.ProjectReadDto;
import com.example.board.rest.dto.project.ProjectStatus;
import com.example.board.rest.dto.project.ProjectUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ProjectMapper projectMapper;


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
        projectEntity = projectRepository.save(projectEntity);
        return projectEntity.getId();
    }

    @Override
    public void update(long id, ProjectUpdateDto project) throws BoardAppIncorrectIdException {
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no project with id = %d", id))
        );

        PersonEntity personEntity = personRepository.findById(project.getCustomerId()).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no customer with id = %d", project.getCustomerId()))
        );

        projectEntity.setName(project.getName());
        projectEntity.setDescription(project.getDescription());
        projectEntity.setStatus(projectEntity.getStatus());
        projectEntity.setCustomer(personEntity);
        projectRepository.save(projectEntity);

    }

    @Override
    public void delete(long id) throws BoardAppIncorrectIdException {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new BoardAppIncorrectIdException(String.format("There is no project with id = %d", id));
        }
    }
}
