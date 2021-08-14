package com.example.board.service;

import com.example.board.rest.dto.project.ProjectCreateDto;
import com.example.board.rest.dto.project.ProjectReadDto;
import com.example.board.rest.dto.project.ProjectUpdateDto;

import java.util.List;

/**
 * Interface for working with projects.
 */
public interface ProjectService {
    /**
     * Searches and returns information about the project (if found).
     * @param id An id of project to search for
     * @return A project description
     */
    ProjectReadDto getById(long id);

    /**
     * Retrieving a complete list of projects.
     * @return A list of existing projects
     */
    List<ProjectReadDto> getAll();

    /**
     * Used to add a new project.
     * @param project A DTO with parameters required to create a new project
     * @return An id of project created
     */
    long add(ProjectCreateDto project);

    /*void update(long id,
                Optional<String> name,
                Optional<String> description,
                Optional<Long> customerId,
                Optional<ProjectStatus> status);*/

    /**
     * Used to update project information.
     * @param id An id of project to search for
     * @param projectUpdateDto A DTO with parameters required to update a project
     */
    void update(long id, ProjectUpdateDto projectUpdateDto);

    /**
     * Deletes a project from the system.
     * @param id An id of project to search for
     */
    void delete(long id);
}
