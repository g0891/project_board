package com.example.board.rest.controller;

import com.example.board.rest.dto.IdDTO;
import com.example.board.rest.dto.release.ReleaseCreateDTO;
import com.example.board.rest.dto.release.ReleaseReadDTO;
import com.example.board.rest.dto.release.ReleaseStatus;
import com.example.board.rest.dto.release.ReleaseUpdateDTO;
import com.example.board.rest.errorController.exception.IncorrectIdException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/management/releases")
@Tag(name="Контроллер работы с релизами", description = "Позволяет создавать, просматривать и удалять релизы")
public class ReleaseController {
    @GetMapping(path = "")
    @Operation(summary = "Список релизов", description = "Позволяет получить полный список релизов")
    public ResponseEntity<List<ReleaseReadDTO>> getReleases() {
        ReleaseReadDTO r1 = new ReleaseReadDTO(1, "7.0", 21, ReleaseStatus.CLOSED,
                LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(4));
        ReleaseReadDTO r2 = new ReleaseReadDTO(2, "7.1", 21, ReleaseStatus.CLOSED,
                LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(2));
        ReleaseReadDTO r3 = new ReleaseReadDTO(3, "8.0", 21, ReleaseStatus.OPEN,
                LocalDateTime.now().minusDays(1), null);

        return ResponseEntity.ok().body(List.of(r1,r2,r3));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Прочитать релиз", description = "Позволяет получить описание релиза")
    public ResponseEntity<ReleaseReadDTO> getRelease(@PathVariable @Parameter(description = "Идентификатор релиза") int id) throws Exception{
        if (id <= 0) throw new IncorrectIdException();
        ReleaseReadDTO resp =
                new ReleaseReadDTO(id, "7.0", 21, ReleaseStatus.OPEN, LocalDateTime.now(), null);
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "")
    @Operation(summary = "Создать релиз", description = "Позволяет создать новый релиз")
    public ResponseEntity<IdDTO> newRelease(@RequestBody @Parameter(description = "Описание нового релиза") ReleaseCreateDTO release) {
        return ResponseEntity.ok(new IdDTO(55));
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Обновить релиз", description = "Позволяет обновить данные по релизу")
    public ResponseEntity updateRelease(@PathVariable @Parameter(description = "Идентификатор релиза") int id, @RequestBody @Parameter(description = "Описание обновления релиза") ReleaseUpdateDTO release) throws Exception {
        if (id <= 0) throw new IncorrectIdException();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Удалить релиз", description = "Позволяет удалить релиз")
    public ResponseEntity deleteRelease(@PathVariable @Parameter(description = "Идентификатор релиза") int id) throws Exception{
        if (id <= 0) throw new IncorrectIdException();
        return ResponseEntity.ok().build();
    }
}
