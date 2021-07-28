package com.example.board.rest.controller;

import com.example.board.rest.dto.project.ProjectCreateDto;
import com.example.board.rest.dto.project.ProjectReadDto;
import com.example.board.rest.dto.project.ProjectUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import com.example.board.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("projects")
@Tag(name = "Контроллер работы с проектами", description = "Позволяет создавать, просматривать и удалять проекты")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping
    @Operation(summary = "Список проектов", description = "Позволяет получить полный список проектов")
    public ResponseEntity<List<ProjectReadDto>> getProjects() {
        return ResponseEntity.ok().body(projectService.getAll());
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Прочитать проект", description = "Позволяет получить описание проекта")
    public ResponseEntity<ProjectReadDto> getProject(@PathVariable @Parameter(description = "Идентификатор проекта") long id) throws Exception {
        return ResponseEntity.ok().body(projectService.getById(id));
    }


    @PostMapping
    @Operation(summary = "Создать проект", description = "Позволяет создать новый проект")
    public ResponseEntity<Long> newProject(@RequestBody ProjectCreateDto project) throws BoardAppIncorrectIdException {
        return ResponseEntity.ok().body(projectService.add(project));
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Обновить проект", description = "Позволяет обновить данные по проекту")
    public ResponseEntity updateProject(@PathVariable @Parameter(description = "Идентификатор проекта") long id, @RequestBody ProjectUpdateDto project)
            throws BoardAppIncorrectIdException, BoardAppIncorrectEnumException {
        projectService.update(id, project);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удалить проект", description = "Позволяет пудалить проект")
    public ResponseEntity deleteProject(@PathVariable @Parameter(description = "Идентификатор проекта") long id) throws Exception{
        projectService.delete(id);
        return ResponseEntity.ok().build();
    }


}
