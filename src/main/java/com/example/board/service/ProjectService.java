package com.example.board.service;

import com.example.board.rest.dto.project.ProjectCreateDto;
import com.example.board.rest.dto.project.ProjectReadDto;
import com.example.board.rest.dto.project.ProjectStatus;
//import com.example.board.rest.dto.project.ProjectUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    ProjectReadDto getById(long id) throws BoardAppIncorrectIdException;
    List<ProjectReadDto> getAll();
    long add(ProjectCreateDto project) throws BoardAppIncorrectIdException;
    //void update(long id, ProjectUpdateDto project) throws BoardAppIncorrectIdException;
    void update(long id, Optional<String> name, Optional<String> description, Optional<Long> customerId, Optional<ProjectStatus> status);
    void delete(long id) throws BoardAppIncorrectIdException;
}
