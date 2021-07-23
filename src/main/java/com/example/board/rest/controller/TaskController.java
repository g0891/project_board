package com.example.board.rest.controller;

import com.example.board.rest.dto.task.TaskCreateDto;
import com.example.board.rest.dto.task.TaskReadDto;
import com.example.board.rest.dto.task.TaskStatus;
import com.example.board.rest.dto.task.TaskUpdateDto;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("tasks")
@Tag(name = "Контроллер работы с задачами", description = "Позволяет создавать, просматривать и удалять задачи")
public class TaskController {
    @GetMapping
    @Operation(summary = "Список задач", description = "Позволяет получить полный список задач")
    public ResponseEntity<List<TaskReadDto>> getTasks(){
        TaskReadDto t1 = new TaskReadDto(1,"Task1", "Desc 1", TaskStatus.BACKLOG,
                1, 2, 12, LocalDateTime.now(), null);
        TaskReadDto t2 = new TaskReadDto(2,"Task2", "Desc 2", TaskStatus.IN_PROGRESS,
                1, 2, 12, LocalDateTime.now(), null);
        TaskReadDto t3 = new TaskReadDto(3,"Task3", "Desc 3", TaskStatus.DONE,
                1, 2, 12, LocalDateTime.now(), null);
        return ResponseEntity.ok(List.of(t1,t2,t3));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Прочитать задачу", description = "Позволяет получить описание задачи")
    public ResponseEntity<TaskReadDto> getTask(@PathVariable @Parameter(description = "Идентификатор задачи") int id) throws Exception {
        if (id <= 0) {
            throw new IncorrectIdFormatException();
        }
        TaskReadDto t1 = new TaskReadDto(id,"Task1", "Desc 1", TaskStatus.BACKLOG,
                1, 2, 12, LocalDateTime.now().minusDays(100), null);
        return ResponseEntity.ok(t1);
    }

    @PostMapping
    @Operation(summary = "Создать задачу", description = "Позволяет создать новую задачу")
    public ResponseEntity<Integer> newTask(@RequestBody TaskCreateDto task) {
        return ResponseEntity.ok().body(99);
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Обновить данные задачи", description = "Позволяет обновить данные по задаче")
    public ResponseEntity updateTask(@PathVariable @Parameter(description = "Идентификатор задачи") int id, @RequestBody TaskUpdateDto task) throws Exception {
        if (id <= 0) {
            throw new IncorrectIdFormatException();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Удалить задачу", description = "Позволяет удалить задачу")
    public ResponseEntity deleteTask(@PathVariable @Parameter(description = "Идентификатор задачи")int id) throws Exception {
        if (id <= 0) {
            throw new IncorrectIdFormatException();
        }
        return ResponseEntity.ok().build();
    }
}
