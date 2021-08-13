package com.example.board.rest.controller;


import com.example.board.rest.dto.release.ReleaseCreateDto;
import com.example.board.rest.dto.release.ReleaseReadDto;
import com.example.board.rest.dto.release.ReleaseUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.service.ReleaseService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequestMapping("releases")
@RequestMapping("${api.path}/releases")
@Tag(name = "Контроллер работы с релизами", description = "Позволяет создавать, просматривать и удалять релизы")
public class ReleaseController {

    private final static Logger log = LoggerFactory.getLogger(ReleaseController.class);

    private final ReleaseService releaseService;

    //@Autowired
    public ReleaseController(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }


    @GetMapping
    @PreAuthorize("hasAuthority('releases:read')")
    @Operation(summary = "Список релизов", description = "Позволяет получить полный список релизов")
    public ResponseEntity<List<ReleaseReadDto>> getReleases() {
        log.info("Release list requested");
        List<ReleaseReadDto> list = releaseService.getAll();
        log.info("Release list provided");
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('releases:read')")
    @Operation(summary = "Прочитать релиз", description = "Позволяет получить описание релиза")
    public ResponseEntity<ReleaseReadDto> getRelease(@PathVariable @Parameter(description = "Идентификатор релиза") long id) {
        log.info("Release info requested for release id = {}", id);
        ReleaseReadDto releaseReadDto = releaseService.getById(id);
        log.info("Release info provided for release id = {}", id);
        return ResponseEntity.ok().body(releaseReadDto);

    }

    @GetMapping(path = "/{id}/tasksCancelled")
    @Operation(summary = "Посчитать незавершившиеся задачи", description = "Позволяет получить количество незавершившися в заданный релиз задач.")
    public ResponseEntity<Long> countCanceledTasksNumber(@PathVariable @Parameter(description = "Идентификатор релиза") long id) {
        log.info("Release info for counting cancelled tasks requested for release id = {}", id);
        Long count = releaseService.countCancelledForClosedRelease(id);
        log.info("Release info for counting cancelled tasks provided for release id = {}", id);
        return ResponseEntity.ok().body(count);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('releases:write')")
    @Operation(summary = "Создать релиз", description = "Позволяет создать новый релиз")
    public ResponseEntity<Long> newRelease(@RequestBody ReleaseCreateDto release) throws BoardAppIncorrectIdException {
        log.info("Release creation requested.");
        Long id = releaseService.add(release);
        log.info("Release created with id = {}", id);
        return ResponseEntity.ok().body(id);
    }

    /*@PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('releases:write')")
    @Operation(summary = "Обновить релиз", description = "Позволяет обновить данные по релизу")
    public ResponseEntity updateRelease(@PathVariable @Parameter(description = "Идентификатор релиза") long id,
                                        @RequestParam @Parameter(description = "Версия релиза (опционально)") Optional<String> version,
                                        @RequestParam @Parameter(description = "Статус релиза (опционально)") Optional<ReleaseStatus> status) {
        log.info("Release update requested for id = {}", id);
        releaseService.update(id, version, status);
        log.info("Release update done for id = {}", id);
        return ResponseEntity.ok().build();
    }*/

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('releases:write')")
    @Operation(summary = "Обновить релиз", description = "Позволяет обновить данные по релизу")
    public ResponseEntity updateRelease(@PathVariable @Parameter(description = "Идентификатор релиза") long id,
                                        @RequestBody ReleaseUpdateDto releaseUpdateDto) {
        log.info("Release update requested for id = {}", id);
        releaseService.update(id, releaseUpdateDto);
        log.info("Release update done for id = {}", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('releases:write')")
    @Operation(summary = "Удалить релиз", description = "Позволяет удалить релиз")
    public ResponseEntity deleteRelease(@PathVariable @Parameter(description = "Идентификатор релиза") long id) {
        log.info("Release deletion requested for id = {}", id);
        releaseService.delete(id);
        log.info("Release id = {} deleted", id);
        return ResponseEntity.ok().build();
    }
}
