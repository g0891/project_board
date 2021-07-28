package com.example.board.rest.controller;


import com.example.board.rest.dto.release.ReleaseCreateDto;
import com.example.board.rest.dto.release.ReleaseReadDto;
import com.example.board.rest.dto.release.ReleaseUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.service.ReleaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("releases")
@Tag(name = "Контроллер работы с релизами", description = "Позволяет создавать, просматривать и удалять релизы")
public class ReleaseController {

    @Autowired
    ReleaseService releaseService;

    @GetMapping
    @Operation(summary = "Список релизов", description = "Позволяет получить полный список релизов")
    public ResponseEntity<List<ReleaseReadDto>> getReleases() {
        return ResponseEntity.ok().body(releaseService.getAll());
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Прочитать релиз", description = "Позволяет получить описание релиза")
    public ResponseEntity<ReleaseReadDto> getRelease(@PathVariable @Parameter(description = "Идентификатор релиза") long id) throws Exception{
        return ResponseEntity.ok().body(releaseService.getById(id));

    }

    @PostMapping
    @Operation(summary = "Создать релиз", description = "Позволяет создать новый релиз")
    public ResponseEntity<Long> newRelease(@RequestBody ReleaseCreateDto release) throws BoardAppIncorrectIdException {
        return ResponseEntity.ok().body(releaseService.add(release));
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Обновить релиз", description = "Позволяет обновить данные по релизу")
    public ResponseEntity updateRelease(@PathVariable @Parameter(description = "Идентификатор релиза") long id, @RequestBody ReleaseUpdateDto release) throws Exception {
        releaseService.update(id, release);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Удалить релиз", description = "Позволяет удалить релиз")
    public ResponseEntity deleteRelease(@PathVariable @Parameter(description = "Идентификатор релиза") long id) throws Exception{
        releaseService.delete(id);
        return ResponseEntity.ok().build();
    }
}
