package com.example.board.rest.controller;

import com.example.board.rest.dto.task.TaskCreateDto;
import com.example.board.rest.dto.task.TaskReadDto;
import com.example.board.rest.dto.task.TaskUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.service.TaskService;
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
@RequestMapping("tasks")
@Tag(name = "Контроллер работы с задачами", description = "Позволяет создавать, просматривать и удалять задачи")
public class TaskController {

    @Autowired
    TaskService taskService;

    @GetMapping
    @Operation(summary = "Список задач", description = "Позволяет получить полный список задач")
    public ResponseEntity<List<TaskReadDto>> getTasks(){
        return ResponseEntity.ok().body(taskService.getAll());
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Прочитать задачу", description = "Позволяет получить описание задачи")
    public ResponseEntity<TaskReadDto> getTask(@PathVariable @Parameter(description = "Идентификатор задачи") long id) throws Exception {
        return ResponseEntity.ok().body(taskService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Создать задачу", description = "Позволяет создать новую задачу")
    public ResponseEntity<Long> newTask(@RequestBody TaskCreateDto task) throws BoardAppIncorrectIdException {
        return ResponseEntity.ok().body(taskService.add(task));
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Обновить данные задачи", description = "Позволяет обновить данные по задаче")
    public ResponseEntity updateTask(@PathVariable @Parameter(description = "Идентификатор задачи") long id, @RequestBody TaskUpdateDto task) throws Exception {
        taskService.update(id, task);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Удалить задачу", description = "Позволяет удалить задачу")
    public ResponseEntity deleteTask(@PathVariable @Parameter(description = "Идентификатор задачи") long id) throws Exception {
        taskService.delete(id);
        return ResponseEntity.ok().build();
    }
}
