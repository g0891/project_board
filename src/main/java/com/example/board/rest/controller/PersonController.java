package com.example.board.rest.controller;

import com.example.board.rest.dto.person.PersonCreateDto;
import com.example.board.rest.dto.person.PersonReadDto;
import com.example.board.rest.dto.person.PersonRole;
import com.example.board.rest.dto.person.PersonUpdateDto;
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

import java.util.List;

@RestController
//@RequestMapping("api/management/persons")
@RequestMapping("persons")
@Tag(name = "Контроллер работы с пользователями", description = "Позволяет создавать, просматривать и удалять пользователя")
public class PersonController {

    @GetMapping
    @Operation(summary = "Список пользователей", description = "Позволяет получить полный ссписок пользователей")
    public ResponseEntity<List<PersonReadDto>> getPersons() {
        return ResponseEntity.ok().body(List.of(
                new PersonReadDto(1, "John",List.of(PersonRole.AUTHOR, PersonRole.EXECUTOR)),
                new PersonReadDto(2, "Ann",List.of(PersonRole.CUSTOMER))
                ));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Описание пользователя", description = "Позволяет получить данные пользователя")
    public ResponseEntity<PersonReadDto> getPerson(@PathVariable @Parameter(description = "Идентификатор пользователя") long id) throws Exception {
        if (id <= 0) {
            throw new IncorrectIdFormatException();
        }
        return ResponseEntity.ok().body(new PersonReadDto(id, "Some user", List.of(PersonRole.CUSTOMER)));
    }

    @PostMapping
    @Operation(summary = "Создать пользователя", description = "Позволяет создать нового пользователя")
    public ResponseEntity<Long> newPerson(@RequestBody PersonCreateDto person) {
        return ResponseEntity.ok().body(99L);
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Обновить данные пользователя", description = "Позволяет обновить данные пользователя")
    public ResponseEntity updatePerson(@PathVariable @Parameter(description = "Идентификатор пользователя") long id, @RequestBody PersonUpdateDto person) throws Exception {
        if (id <= 0) {
            throw new IncorrectIdFormatException();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Удалить пользователя", description = "Позволяет удалить пользователя")
    public ResponseEntity deletePerson(@PathVariable @Parameter(description = "Идентификатор пользователя") long id) throws Exception {
        if (id <= 0) {
            throw new IncorrectIdFormatException();
        }
        return ResponseEntity.ok().build();
    }

}
