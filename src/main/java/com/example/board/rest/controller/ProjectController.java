package com.example.board.rest.controller;

import com.example.board.rest.dto.project.ProjectCreateDto;
import com.example.board.rest.dto.project.ProjectReadDto;
import com.example.board.rest.dto.project.ProjectUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequestMapping("projects")
@RequestMapping("${api.path}/projects")
@Tag(name = "Контроллер работы с проектами", description = "Позволяет создавать, просматривать и удалять проекты")
public class ProjectController {

    private final static Logger log = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;

//    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('projects:read')")
    @Operation(summary = "Список проектов", description = "Позволяет получить полный список проектов")
    public ResponseEntity<List<ProjectReadDto>> getProjects() {
        log.info("Project list requested");
        List<ProjectReadDto> list = projectService.getAll();
        log.info("Project list provided");
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('projects:read')")
    @Operation(summary = "Прочитать проект", description = "Позволяет получить описание проекта")
    public ResponseEntity<ProjectReadDto> getProject(@PathVariable @Parameter(description = "Идентификатор проекта") long id) {
        log.info("Project info requested for project id = {}", id);
        ProjectReadDto projectReadDto = projectService.getById(id);
        log.info("Project info provided for project id = {}", id);
        return ResponseEntity.ok().body(projectReadDto);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('projects:write')")
    @Operation(summary = "Создать проект", description = "Позволяет создать новый проект")
    public ResponseEntity<Long> newProject(@RequestBody ProjectCreateDto project) throws BoardAppIncorrectIdException {
        log.info("Project creation requested.");
        Long id = projectService.add(project);
        log.info("Project created with id = {}", id);
        return ResponseEntity.ok().body(id);
    }

    /*@PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('projects:write')")
    @Operation(summary = "Обновить проект", description = "Позволяет обновить данные по проекту")
    public ResponseEntity updateProject(@PathVariable @Parameter(description = "Идентификатор проекта") long id,
                                        @RequestParam @Parameter(description = "Название проекта (опционально)") Optional<String> name,
                                        @RequestParam @Parameter(description = "Описание проекта (опционально)") Optional<String> description,
                                        @RequestParam @Parameter(description = "Идентификатор клиента (опционально)") Optional<Long> customerId,
                                        @RequestParam @Parameter(description = "Статус проекта (опционально)") Optional<ProjectStatus> status)
            throws BoardAppIncorrectIdException, BoardAppIncorrectEnumException {
        log.info("Project update requested for id = {}", id);
        projectService.update(id, name, description, customerId, status);
        log.info("Project update done for id = {}", id);
        return ResponseEntity.ok().build();
    }*/

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('projects:write')")
    @Operation(summary = "Обновить проект", description = "Позволяет обновить данные по проекту")
    public ResponseEntity updateProject(@PathVariable @Parameter(description = "Идентификатор проекта") long id,
                                        @RequestBody ProjectUpdateDto projectUpdateDto)
            throws BoardAppIncorrectIdException, BoardAppIncorrectEnumException {
        log.info("Project update requested for id = {}", id);
        projectService.update(id, projectUpdateDto);
        log.info("Project update done for id = {}", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('projects:write')")
    @Operation(summary = "Удалить проект", description = "Позволяет пудалить проект")
    public ResponseEntity deleteProject(@PathVariable @Parameter(description = "Идентификатор проекта") long id) {
        log.info("Project deletion requested for id = {}", id);
        projectService.delete(id);
        log.info("Project id = {} deleted", id);
        return ResponseEntity.ok().build();
    }


}
