package com.example.board.rest.controller;

import com.example.board.rest.dto.person.PersonCreateDTO;
import com.example.board.rest.dto.person.PersonReadDTO;
import com.example.board.rest.dto.person.PersonRole;
import com.example.board.rest.errorController.exception.IncorrectIdException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/management/persons")
@Tag(name="Контроллер работы с пользователями", description = "Позволяет создавать, просматривать и удалять пользователя")
public class PersonController {

    @GetMapping(path = "")
    @Operation(summary = "Список пользователей", description = "Позволяет получить полный ссписок пользователей")
    public ResponseEntity<List<PersonReadDTO>> getPersons() {
        return ResponseEntity.ok().body(List.of(
                new PersonReadDTO(1, "John",List.of(PersonRole.AUTHOR, PersonRole.EXECUTOR)),
                new PersonReadDTO(2, "Ann",List.of(PersonRole.CUSTOMER))
                ));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Описание пользователя", description = "Позволяет получить данные пользователя")
    public ResponseEntity<PersonReadDTO> getPerson(@PathVariable @Parameter(description = "Идентификатор пользователя") int id) throws Exception {
        if (id <= 0) throw new IncorrectIdException();
        return ResponseEntity.ok().body(new PersonReadDTO(id, "Some user", List.of(PersonRole.CUSTOMER)));
    }

    @PostMapping(path = "")
    @Operation(summary = "Создать пользователя", description = "Позволяет создать нового пользователя")
    public ResponseEntity<PersonReadDTO> newPerson(@RequestBody @Parameter(description = "Описание нового пользователя") PersonCreateDTO person) {
        return ResponseEntity.ok().body(new PersonReadDTO(888, person));
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Удалить пользователя", description = "Позволяет удалить пользователя")
    public ResponseEntity deletePerson(@PathVariable @Parameter(description = "Идентификатор пользователя") int id) throws Exception {
        if (id <= 0) throw new IncorrectIdException();
        return ResponseEntity.ok().build();
    }

}
