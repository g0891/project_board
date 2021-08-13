package com.example.board.rest.controller;

import com.example.board.rest.dto.task.TaskCreateDto;
import com.example.board.rest.dto.task.TaskReadDto;
import com.example.board.rest.dto.task.TaskSearchDto;
import com.example.board.rest.dto.task.TaskUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.service.TaskService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
//@RequestMapping("tasks")
@RequestMapping("${api.path}/tasks")
@Tag(name = "Контроллер работы с задачами", description = "Позволяет создавать, просматривать и удалять задачи")
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    //@Autowired
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('tasks:read')")
    @Operation(summary = "Список задач", description = "Позволяет получить полный список задач")
    public ResponseEntity<List<TaskReadDto>> getTasks(){
        log.info("Task list requested");
        List<TaskReadDto> tasks = taskService.getAll();
        log.info("Task list provided");
        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('tasks:read')")
    @Operation(summary = "Прочитать задачу", description = "Позволяет получить описание задачи")
    public ResponseEntity<TaskReadDto> getTask(@PathVariable @Parameter(description = "Идентификатор задачи") long id) {
        log.info("Task info requested for task id = {}", id);
        TaskReadDto task = taskService.getById(id);
        log.info("Task info provided for task id = {}", id);
        return ResponseEntity.ok().body(task);
    }

   /* @GetMapping(path = "/filtering")
    @PreAuthorize("hasAuthority('tasks:read')")
    @Operation(summary = "Список задач", description = "Позволяет получить отфильтрованный список задач")
    public ResponseEntity<List<TaskReadDto>> getTasksFiltered(
            @RequestParam @Parameter(description = "Идентификатор задачи (опционально)") Optional<Long> id,
            @RequestParam @Parameter(description = "Часть имени задачи (опционально)") Optional<String> name,
            @RequestParam @Parameter(description = "Часть описания задачи (опционально)") Optional<String> description,
            @RequestParam @Parameter(description = "Статус задачи (опционально)") Optional<TaskStatus> status,
            @RequestParam @Parameter(description = "Идентификатор автора задачи (опционально)") Optional<Long> authorId,
            @RequestParam @Parameter(description = "Идентификатор исполнителя задачи (опционально)") Optional<Long> executorId,
            @RequestParam @Parameter(description = "Идентификатор релиза, в который входит задача (опционально)") Optional<Long> releaseId,
            @RequestParam @Parameter(description = "Идентификатор проекта, в который входит задача (опционально)") Optional<Long> projectId
    ) {
        log.info("Task list filtered requested");
        List<TaskReadDto> taskReadDtoList = taskService.getFiltered(
                id.orElse(null),
                name.orElse(null),
                description.orElse(null),
                status.orElse(null),
                authorId.orElse(null),
                executorId.orElse(null),
                releaseId.orElse(null),
                projectId.orElse(null)
        );
        log.info("Task list filtered provided.");
        return ResponseEntity.ok().body(taskReadDtoList);
    }*/

    @PostMapping("/filtering")
    @PreAuthorize("hasAuthority('tasks:read')")
    @Operation(summary = "Список задач", description = "Позволяет получить отфильтрованный список задач")
    public ResponseEntity<List<TaskReadDto>> getTasksFiltered(@RequestBody TaskSearchDto taskSearchDto) {
        log.info("Task list filtered requested");
        List<TaskReadDto> taskReadDtoList = taskService.getFiltered(taskSearchDto);
        log.info("Task list filtered provided.");
        return ResponseEntity.ok().body(taskReadDtoList);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('tasks:write')")
    @Operation(summary = "Создать задачу", description = "Позволяет создать новую задачу")
    public ResponseEntity<Long> newTask(@RequestBody TaskCreateDto task) throws BoardAppIncorrectIdException {
        log.info("Task creation requested.");
        Long id = taskService.add(task);
        log.info("Task created with id = {}", id);
        return ResponseEntity.ok().body(id);
    }

    @PostMapping("/uploadCSV")
    @PreAuthorize("hasAuthority('tasks:write')")
    @Operation(summary = "Создать задачу", description = "Позволяет создать новую задачу путем загрузки CSV файла")
    public ResponseEntity<Long> newTaskFromCsv(@RequestParam("file") MultipartFile file){
        log.info("Creating task from CSV.");
        Long id = taskService.add(file);
        log.info("Task with id = {} created", id);
        return ResponseEntity.ok().body(id);

    }

    /*@PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('tasks:write')")
    @Operation(summary = "Обновить данные задачи", description = "Позволяет обновить данные по задаче")
    public ResponseEntity<String> updateTaskOpt(@PathVariable @Parameter(description = "Идентификатор задачи") long id,
                                        @Parameter(description = "Имя задачи (опционально)") @RequestParam Optional<String> name,
                                        @Parameter(description = "Описание задачи (опционально)") @RequestParam Optional<String> description,
                                        @Parameter(description = "Статус задачи (опционально)") @RequestParam Optional<TaskStatus> status,
                                        @Parameter(description = "Идентификатор исполнителя задачи (опционально)") @RequestParam Optional<Long> executorId
                                    ) {
        log.info("Task update requested for id = {}", id);
        taskService.update(id, name, description, status, executorId);
        log.info("Task update done for id = {}", id);
        return ResponseEntity.ok().build();
    }*/

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('tasks:write')")
    @Operation(summary = "Обновить данные задачи", description = "Позволяет обновить данные по задаче")
    public ResponseEntity<String> updateTaskOpt(@PathVariable @Parameter(description = "Идентификатор задачи") long id,
                                                @RequestBody TaskUpdateDto taskUpdateDto
    ) {
        log.info(String.format("Task update requested for id = %d", id));
        taskService.update(id, taskUpdateDto);
        log.info(String.format("Task update done for id = %d", id));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('tasks:write')")
    @Operation(summary = "Удалить задачу", description = "Позволяет удалить задачу")
    public ResponseEntity deleteTask(@PathVariable @Parameter(description = "Идентификатор задачи") long id) {
        log.info("Task deletion requested for id = {}", id);
        taskService.delete(id);
        log.info("Task id = {} deleted", id);
        return ResponseEntity.ok().build();
    }
}
