package com.example.board.rest.controller;


import com.example.board.rest.dto.release.ReleaseCreateDto;
import com.example.board.rest.dto.release.ReleaseReadDto;
import com.example.board.rest.dto.release.ReleaseStatus;
//import com.example.board.rest.dto.release.ReleaseUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.service.ReleaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("releases")
@Tag(name = "Контроллер работы с релизами", description = "Позволяет создавать, просматривать и удалять релизы")
public class ReleaseController {

    private final static Logger log = LoggerFactory.getLogger(ReleaseController.class);

    private final ReleaseService releaseService;

    @Autowired
    public ReleaseController(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }


    @GetMapping
    @Operation(summary = "Список релизов", description = "Позволяет получить полный список релизов")
    public ResponseEntity<List<ReleaseReadDto>> getReleases() {
        log.info("Release list requested");
        List<ReleaseReadDto> list = releaseService.getAll();
        log.info("Release list provided");
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Прочитать релиз", description = "Позволяет получить описание релиза")
    public ResponseEntity<ReleaseReadDto> getRelease(@PathVariable @Parameter(description = "Идентификатор релиза") long id) throws Exception{
        log.info(String.format("Release info requested for release id = %d", id));
        ReleaseReadDto releaseReadDto = releaseService.getById(id);
        log.info(String.format("Release info provided for release id = %d", id));
        return ResponseEntity.ok().body(releaseReadDto);

    }

    @GetMapping(path = "/{id}/number-of-cancelled-tasks")
    @Operation(summary = "Посчитать незавершившиеся задачи", description = "Позволяет получить количество незавершившися в заданный релиз задач.")
    public ResponseEntity<Long> countCanceledTasksNumber(@PathVariable @Parameter(description = "Идентификатор релиза") long id) throws Exception {
        log.info(String.format("Release info for counting cancelled tasks requested for release id = %d", id));
        Long count = releaseService.countCancelledForClosedRelease(id);
        log.info(String.format("Release info for counting cancelled tasks provided for release id = %d", id));
        return ResponseEntity.ok().body(count);
    }

    @PostMapping
    @Operation(summary = "Создать релиз", description = "Позволяет создать новый релиз")
    public ResponseEntity<Long> newRelease(@RequestBody ReleaseCreateDto release) throws BoardAppIncorrectIdException {
        log.info("Release creation requested.");
        Long id = releaseService.add(release);
        log.info(String.format("Release created with id = %d", id));
        return ResponseEntity.ok().body(id);
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Обновить релиз", description = "Позволяет обновить данные по релизу")
    public ResponseEntity updateRelease(@PathVariable @Parameter(description = "Идентификатор релиза") long id,
                                        @RequestParam @Parameter(description = "Версия релиза (опционально)") Optional<String> version,
                                        @RequestParam @Parameter(description = "Статус релиза (опционально)") Optional<ReleaseStatus> status
                                        /*@RequestBody ReleaseUpdateDto release*/) throws Exception {
        log.info(String.format("Release update requested for id = %d", id));
        releaseService.update(id, version, status);
        log.info(String.format("Release update done for id = %d", id));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Удалить релиз", description = "Позволяет удалить релиз")
    public ResponseEntity deleteRelease(@PathVariable @Parameter(description = "Идентификатор релиза") long id) throws Exception{
        log.info(String.format("Release deletion requested for id = %d", id));
        releaseService.delete(id);
        log.info(String.format("Release id = %d deleted", id));
        return ResponseEntity.ok().build();
    }
}
