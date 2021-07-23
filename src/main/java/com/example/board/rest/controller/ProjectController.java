package com.example.board.rest.controller;

import com.example.board.rest.dto.project.ProjectCreateDto;
import com.example.board.rest.dto.project.ProjectReadDto;
import com.example.board.rest.dto.project.ProjectStatus;
import com.example.board.rest.dto.project.ProjectUpdateDto;
import com.example.board.rest.errorController.exception.IncorrectIdException;
import com.example.board.rest.errorController.exception.IncorrectIdFormatException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
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

    @GetMapping
    @Operation(summary = "Список проектов", description = "Позволяет получить полный список проектов")
    public ResponseEntity<List<ProjectReadDto>> getProjects() {
        ProjectReadDto p1 = new ProjectReadDto(1, "project 1", "p1 description", 123, ProjectStatus.OPEN);
        ProjectReadDto p2 = new ProjectReadDto(2, "project 2", "p2 description", 234, ProjectStatus.OPEN);
        ProjectReadDto p3 = new ProjectReadDto(3, "project 3", "p3 description", 345, ProjectStatus.OPEN);
        return ResponseEntity.ok().body(List.of(p1,p2,p3));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Прочитать проект", description = "Позволяет получить описание проекта")
    public ResponseEntity<ProjectReadDto> getProject(@PathVariable @Parameter(description = "Идентификатор проекта") long id) throws Exception {
        if (id <= 0) {
            throw new IncorrectIdFormatException();
        }
        ProjectReadDto p1 = new ProjectReadDto(id, "project 1", "p1 description", 123, ProjectStatus.OPEN);
        return ResponseEntity.ok(p1);
    }


    @PostMapping
    @Operation(summary = "Создать проект", description = "Позволяет создать новый проект")
    public ResponseEntity<Long> newProject(@RequestBody ProjectCreateDto project) {
        return ResponseEntity.ok().body(88L);
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Обновить проект", description = "Позволяет обновить данные по проекту")
    public ResponseEntity updateProject(@PathVariable @Parameter(description = "Идентификатор проекта") long id, @RequestBody ProjectUpdateDto project) throws Exception {
        if (id <= 0) {
            throw new IncorrectIdFormatException();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удалить проект", description = "Позволяет пудалить проект")
    public ResponseEntity deleteProject(@PathVariable @Parameter(description = "Идентификатор проекта") long id) throws Exception{
        if (id <= 0) {
            throw new IncorrectIdFormatException();
        }
        return ResponseEntity.ok().build();
    }


}
