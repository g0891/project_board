package com.example.board.rest.controller;

import com.example.board.rest.dto.person.PersonCreateDto;
import com.example.board.rest.dto.person.PersonReadDto;
import com.example.board.rest.dto.person.PersonRole;
//import com.example.board.rest.dto.person.PersonUpdateDto;
import com.example.board.service.PersonService;
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
import java.util.Set;

@RestController
@RequestMapping("persons")
@Tag(name = "Контроллер работы с пользователями", description = "Позволяет создавать, просматривать и удалять пользователя")
public class PersonController {

/*    @Autowired
    private PersonService personService;*/

    private final static Logger log = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    @Operation(summary = "Список пользователей", description = "Позволяет получить полный ссписок пользователей")
    public ResponseEntity<List<PersonReadDto>> getPersons() {
        log.info("Person list requested");
        List<PersonReadDto> personReadDtoList = personService.getAll();
        log.info("Person list provided");
        return ResponseEntity.ok().body(personReadDtoList);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Описание пользователя", description = "Позволяет получить данные пользователя")
    public ResponseEntity<PersonReadDto> getPerson(@PathVariable @Parameter(description = "Идентификатор пользователя") long id) throws Exception {
        log.info(String.format("Person info requested for person id = %d", id));
        PersonReadDto personReadDto = personService.getById(id);
        log.info(String.format("Person info provided for person id = %d", id));
        return ResponseEntity.ok().body(personReadDto);
    }

    @PostMapping
    @Operation(summary = "Создать пользователя", description = "Позволяет создать нового пользователя")
    public ResponseEntity<Long> newPerson(@RequestBody PersonCreateDto person) {
        log.info("Person creation requested.");
        Long id = personService.add(person);
        log.info(String.format("Person created with id = %d", id));
        return ResponseEntity.ok().body(id);
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Обновить данные пользователя", description = "Позволяет обновить данные пользователя")
    public ResponseEntity updatePerson(@PathVariable @Parameter(description = "Идентификатор пользователя") long id,
                                       @RequestParam @Parameter(description = "Имя пользователя (опционально)") Optional<String> name,
                                       @RequestParam @Parameter(description = "Перечень ролей пользователя (опционально)") Optional<Set<PersonRole>> roles
                                       /*@RequestBody PersonUpdateDto person*/) throws Exception {
        log.info(String.format("Person update requested for id = %d", id));
        personService.update(id, name, roles);
        log.info(String.format("Person update done for id = %d", id));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Удалить пользователя", description = "Позволяет удалить пользователя")
    public ResponseEntity deletePerson(@PathVariable @Parameter(description = "Идентификатор пользователя") long id) throws Exception {
        log.info(String.format("Person deletion requested for id = %d", id));
        personService.delete(id);
        log.info(String.format("Person id = %d deleted", id));
        return ResponseEntity.ok().build();
    }

}
