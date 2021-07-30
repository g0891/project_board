package com.example.board.rest.controller;

import com.example.board.rest.dto.person.PersonCreateDto;
import com.example.board.rest.dto.person.PersonReadDto;
import com.example.board.rest.dto.person.PersonUpdateDto;
import com.example.board.service.PersonService;
import com.example.board.service.implementation.PersonServiceImpl;
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
@RequestMapping("persons")
@Tag(name = "Контроллер работы с пользователями", description = "Позволяет создавать, просматривать и удалять пользователя")
public class PersonController {

    //@Autowired
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    @Operation(summary = "Список пользователей", description = "Позволяет получить полный ссписок пользователей")
    public ResponseEntity<List<PersonReadDto>> getPersons() {
        return ResponseEntity.ok().body(personService.getAll());
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Описание пользователя", description = "Позволяет получить данные пользователя")
    public ResponseEntity<PersonReadDto> getPerson(@PathVariable @Parameter(description = "Идентификатор пользователя") long id) throws Exception {
        return ResponseEntity.ok().body(personService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Создать пользователя", description = "Позволяет создать нового пользователя")
    public ResponseEntity<Long> newPerson(@RequestBody PersonCreateDto person) {
        return ResponseEntity.ok().body(personService.add(person));
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Обновить данные пользователя", description = "Позволяет обновить данные пользователя")
    public ResponseEntity updatePerson(@PathVariable @Parameter(description = "Идентификатор пользователя") long id, @RequestBody PersonUpdateDto person) throws Exception {
        personService.update(id, person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Удалить пользователя", description = "Позволяет удалить пользователя")
    public ResponseEntity deletePerson(@PathVariable @Parameter(description = "Идентификатор пользователя") long id) throws Exception {
        personService.delete(id);
        return ResponseEntity.ok().build();
    }

}
