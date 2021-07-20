package com.example.board.rest.controller;

import com.example.board.rest.dto.IdDTO;
import com.example.board.rest.dto.person.PersonUpdateDTO;
import com.example.board.rest.dto.task.TaskCreateDTO;
import com.example.board.rest.dto.task.TaskReadDTO;
import com.example.board.rest.dto.task.TaskStatus;
import com.example.board.rest.dto.task.TaskUpdateDTO;
import com.example.board.rest.errorController.exception.IncorrectIdException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/management/tasks")
@Tag(name="Контроллер работы с задачами", description = "Позволяет создавать, просматривать и удалять задачи")
public class TaskController {
    @GetMapping(path = "")
    @Operation(summary = "Список задач", description = "Позволяет получить полный список задач")
    public ResponseEntity<List<TaskReadDTO>> getTasks(){
        TaskReadDTO t1 = new TaskReadDTO(1,"Task1", "Desc 1", TaskStatus.BACKLOG,
                1, 2, 12, LocalDateTime.now(), null);
        TaskReadDTO t2 = new TaskReadDTO(2,"Task2", "Desc 2", TaskStatus.IN_PROGRESS,
                1, 2, 12, LocalDateTime.now(), null);
        TaskReadDTO t3 = new TaskReadDTO(3,"Task3", "Desc 3", TaskStatus.DONE,
                1, 2, 12, LocalDateTime.now(), null);
        return ResponseEntity.ok(List.of(t1,t2,t3));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Прочитать задачу", description = "Позволяет получить описание задачи")
    public ResponseEntity<TaskReadDTO> getTask(@PathVariable @Parameter(description = "Идентификатор задачи") int id) throws Exception {
        if (id <= 0) throw new IncorrectIdException();
        TaskReadDTO t1 = new TaskReadDTO(id,"Task1", "Desc 1", TaskStatus.BACKLOG,
                1, 2, 12, LocalDateTime.now().minusDays(100), null);
        return ResponseEntity.ok(t1);
    }

    @PostMapping(path = "")
    @Operation(summary = "Создать задачу", description = "Позволяет создать новую задачу")
    public ResponseEntity<IdDTO> newTask(@RequestBody @Parameter(description = "Описание новой задачи") TaskCreateDTO task) {
        return ResponseEntity.ok().body(new IdDTO(99));
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Обновить данные задачи", description = "Позволяет обновить данные по задаче")
    public ResponseEntity updateTask(@PathVariable @Parameter(description = "Идентификатор задачи") int id, @RequestBody @Parameter(description = "Описание обновления данных задачи") TaskUpdateDTO task) throws Exception {
        if (id <= 0) throw new IncorrectIdException();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Удалить задачу", description = "Позволяет удалить задачу")
    public ResponseEntity deleteTask(@PathVariable @Parameter(description = "Идентификатор задачи")int id) throws Exception {
        if (id <= 0) throw new IncorrectIdException();
        return ResponseEntity.ok().build();
    }
}
