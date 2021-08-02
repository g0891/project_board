package com.example.board.rest.controller;

import com.example.board.rest.dto.task.*;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.service.TaskService;
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
@RequestMapping("tasks")
@Tag(name = "Контроллер работы с задачами", description = "Позволяет создавать, просматривать и удалять задачи")
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Список задач", description = "Позволяет получить полный список задач")
    public ResponseEntity<List<TaskReadDto>> getTasks(){
        log.info("Task list requested");
        List<TaskReadDto> tasks = taskService.getAll();
        log.info("Task list provided");
        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Прочитать задачу", description = "Позволяет получить описание задачи")
    public ResponseEntity<TaskReadDto> getTask(@PathVariable @Parameter(description = "Идентификатор задачи") long id) {
        log.info(String.format("Task info requested for task id = %d", id));
        TaskReadDto task = taskService.getById(id);
        log.info(String.format("Task info provided for task id = %d", id));
        return ResponseEntity.ok().body(task);
    }

    @PostMapping
    @Operation(summary = "Создать задачу", description = "Позволяет создать новую задачу")
    public ResponseEntity<Long> newTask(@RequestBody TaskCreateDto task) throws BoardAppIncorrectIdException {
        log.info("Task creation requested.");
        Long id = taskService.add(task);
        log.info(String.format("Task created with id = %d", id));
        return ResponseEntity.ok().body(id);
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Обновить данные задачи", description = "Позволяет обновить данные по задаче")
    public ResponseEntity<String> updateTaskOpt(@PathVariable @Parameter(description = "Идентификатор задачи") long id,
                                        @Parameter(description = "Имя задачи (опционально)") @RequestParam Optional<String> name,
                                        @Parameter(description = "Описание задачи (опционально)") @RequestParam Optional<String> description,
                                        @Parameter(description = "Статус задачи (опционально)") @RequestParam Optional<TaskStatus> status,
                                        @Parameter(description = "Идентификатор исполнителя задачи (опционально)") @RequestParam Optional<Long> executorId
                                    ) {
        log.info(String.format("Task update requested for id = %d", id));
        taskService.update(id, name, description, status, executorId);
        log.info(String.format("Task update done for id = %d", id));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Удалить задачу", description = "Позволяет удалить задачу")
    public ResponseEntity deleteTask(@PathVariable @Parameter(description = "Идентификатор задачи") long id) {
        log.info(String.format("Task deletion requested for id = %d", id));
        taskService.delete(id);
        log.info(String.format("Task id = %d deleted", id));
        return ResponseEntity.ok().build();
    }
}
