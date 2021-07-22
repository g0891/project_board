package com.example.board.rest.controller;


import com.example.board.rest.dto.release.ReleaseCreateDto;
import com.example.board.rest.dto.release.ReleaseReadDto;
import com.example.board.rest.dto.release.ReleaseStatus;
import com.example.board.rest.dto.release.ReleaseUpdateDto;
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
@RequestMapping("releases")
@Tag(name = "Контроллер работы с релизами", description = "Позволяет создавать, просматривать и удалять релизы")
public class ReleaseController {
    @GetMapping
    @Operation(summary = "Список релизов", description = "Позволяет получить полный список релизов")
    public ResponseEntity<List<ReleaseReadDto>> getReleases() {
        ReleaseReadDto r1 = new ReleaseReadDto(1, "7.0", 21, ReleaseStatus.CLOSED,
                LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(4));
        ReleaseReadDto r2 = new ReleaseReadDto(2, "7.1", 21, ReleaseStatus.CLOSED,
                LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(2));
        ReleaseReadDto r3 = new ReleaseReadDto(3, "8.0", 21, ReleaseStatus.OPEN,
                LocalDateTime.now().minusDays(1), null);

        return ResponseEntity.ok().body(List.of(r1,r2,r3));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Прочитать релиз", description = "Позволяет получить описание релиза")
    public ResponseEntity<ReleaseReadDto> getRelease(@PathVariable @Parameter(description = "Идентификатор релиза") long id) throws Exception{
        if (id <= 0) {
            throw new IncorrectIdFormatException();
        }
        ReleaseReadDto resp =
                new ReleaseReadDto(id, "7.0", 21, ReleaseStatus.OPEN, LocalDateTime.now(), null);
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    @Operation(summary = "Создать релиз", description = "Позволяет создать новый релиз")
    public ResponseEntity<Long> newRelease(@RequestBody ReleaseCreateDto release) {
        return ResponseEntity.ok(55L);
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Обновить релиз", description = "Позволяет обновить данные по релизу")
    public ResponseEntity updateRelease(@PathVariable @Parameter(description = "Идентификатор релиза") long id, @RequestBody ReleaseUpdateDto release) throws Exception {
        if (id <= 0) {
            throw new IncorrectIdFormatException();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Удалить релиз", description = "Позволяет удалить релиз")
    public ResponseEntity deleteRelease(@PathVariable @Parameter(description = "Идентификатор релиза") long id) throws Exception{
        if (id <= 0) {
            throw new IncorrectIdFormatException();
        }
        return ResponseEntity.ok().build();
    }
}
