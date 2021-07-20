package com.example.board.rest.controller;

import com.example.board.rest.dto.IdDTO;
import com.example.board.rest.dto.project.ProjectCreateDTO;
import com.example.board.rest.dto.project.ProjectReadDTO;
import com.example.board.rest.dto.project.ProjectStatus;
import com.example.board.rest.dto.project.ProjectUpdateDTO;
import com.example.board.rest.dto.release.ReleaseUpdateDTO;
import com.example.board.rest.errorController.exception.IncorrectIdException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/management/projects")
@Tag(name="Контроллер работы с проектами", description = "Позволяет создавать, просматривать и удалять проекты")
public class ProjectController {

    @GetMapping(path = "", produces = "application/json")
    @Operation(summary = "Список проектов", description = "Позволяет получить полный список проектов")
    public ResponseEntity<List<ProjectReadDTO>> getProjects() {
        ProjectReadDTO p1 = new ProjectReadDTO(1, "project 1", "p1 description", 123, ProjectStatus.OPEN);
        ProjectReadDTO p2 = new ProjectReadDTO(2, "project 2", "p2 description", 234, ProjectStatus.OPEN);
        ProjectReadDTO p3 = new ProjectReadDTO(3, "project 3", "p3 description", 345, ProjectStatus.OPEN);
        return ResponseEntity.ok().body(List.of(p1,p2,p3));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Прочитать проект", description = "Позволяет получить описание проекта")
    public ResponseEntity<ProjectReadDTO> getProject(@PathVariable @Parameter(description = "Идентификатор проекта") int id) throws Exception {
        if (id <= 0) throw new IncorrectIdException();
        ProjectReadDTO p1 = new ProjectReadDTO(id, "project 1", "p1 description", 123, ProjectStatus.OPEN);
        return ResponseEntity.ok(p1);
    }


    @PostMapping(path = "")
    @Operation(summary = "Создать проект", description = "Позволяет создать новый проект")
    public ResponseEntity<IdDTO> newProject(@RequestBody @Parameter(description = "Описание нового проекта") ProjectCreateDTO project) {
        return ResponseEntity.ok().body(new IdDTO(88));
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Обновить проект", description = "Позволяет обновить данные по проекту")
    public ResponseEntity updateProject(@PathVariable @Parameter(description = "Идентификатор проекта") int id, @RequestBody @Parameter(description = "Описание обновления проекта") ProjectUpdateDTO project) throws Exception {
        if (id <= 0) throw new IncorrectIdException();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удалить проект", description = "Позволяет пудалить проект")
    public ResponseEntity deleteProject(@PathVariable @Parameter(description = "Идентификатор проекта") int id) throws Exception{
        if (id <= 0) throw new IncorrectIdException();
        return ResponseEntity.ok().build();
    }


}
