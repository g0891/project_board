package com.example.board.rest.controller;

import com.example.board.rest.dto.project.ProjectCreateDto;
import com.example.board.rest.dto.project.ProjectReadDto;
import com.example.board.rest.dto.project.ProjectStatus;
//import com.example.board.rest.dto.project.ProjectUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import com.example.board.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("projects")
@Tag(name = "Контроллер работы с проектами", description = "Позволяет создавать, просматривать и удалять проекты")
public class ProjectController {

    private final static Logger log = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(summary = "Список проектов", description = "Позволяет получить полный список проектов")
    public ResponseEntity<List<ProjectReadDto>> getProjects() {
        log.info("Project list requested");
        List<ProjectReadDto> list = projectService.getAll();
        log.info("Project list provided");
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Прочитать проект", description = "Позволяет получить описание проекта")
    public ResponseEntity<ProjectReadDto> getProject(@PathVariable @Parameter(description = "Идентификатор проекта") long id) {
        log.info(String.format("Project info requested for project id = %d", id));
        ProjectReadDto projectReadDto = projectService.getById(id);
        log.info(String.format("Project info provided for project id = %d", id));
        return ResponseEntity.ok().body(projectReadDto);
    }


    @PostMapping
    @Operation(summary = "Создать проект", description = "Позволяет создать новый проект")
    public ResponseEntity<Long> newProject(@RequestBody ProjectCreateDto project) throws BoardAppIncorrectIdException {
        log.info("Project creation requested.");
        Long id = projectService.add(project);
        log.info(String.format("Project created with id = %d", id));
        return ResponseEntity.ok().body(id);
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Обновить проект", description = "Позволяет обновить данные по проекту")
    public ResponseEntity updateProject(@PathVariable @Parameter(description = "Идентификатор проекта") long id,
                                        @RequestParam @Parameter(description = "Название проекта (опционально)") Optional<String> name,
                                        @RequestParam @Parameter(description = "Описание проекта (опционально)") Optional<String> description,
                                        @RequestParam @Parameter(description = "Идентификатор клиента (опционально)") Optional<Long> customerId,
                                        @RequestParam @Parameter(description = "Статус проекта (опционально)") Optional<ProjectStatus> status)
            throws BoardAppIncorrectIdException, BoardAppIncorrectEnumException {
        log.info(String.format("Project update requested for id = %d", id));
        projectService.update(id, name, description, customerId, status);
        log.info(String.format("Project update done for id = %d", id));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удалить проект", description = "Позволяет пудалить проект")
    public ResponseEntity deleteProject(@PathVariable @Parameter(description = "Идентификатор проекта") long id) {
        log.info(String.format("Project deletion requested for id = %d", id));
        projectService.delete(id);
        log.info(String.format("Project id = %d deleted", id));
        return ResponseEntity.ok().build();
    }


}
