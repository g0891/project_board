package com.example.board.rest.controller;

//import com.example.board.rest.dto.person.PersonCreateDto;

import com.example.board.rest.dto.person.PersonReadDto;
import com.example.board.rest.dto.person.PersonRegisterDto;
import com.example.board.rest.dto.person.PersonUpdateDto;
import com.example.board.service.PersonService;
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
@RequestMapping("${api.path}/persons")
//@RequestMapping("persons")
@Tag(name = "Контроллер работы с пользователями", description = "Позволяет создавать, просматривать и удалять пользователя")
public class PersonController {

    private final static Logger log = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    //@Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('persons:read')")
    @Operation(summary = "Список пользователей", description = "Позволяет получить полный ссписок пользователей")
    public ResponseEntity<List<PersonReadDto>> getPersons() {
        log.info("Person list requested");
        List<PersonReadDto> personReadDtoList = personService.getAll();
        log.info("Person list provided");
        return ResponseEntity.ok().body(personReadDtoList);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('persons:read')")
    @Operation(summary = "Описание пользователя", description = "Позволяет получить данные пользователя")
    public ResponseEntity<PersonReadDto> getPerson(@PathVariable @Parameter(description = "Идентификатор пользователя") long id) {
        //log.info(String.format("Person info requested for person id = %d", id));
        log.info("Person info requested for person id = {}", id);
        PersonReadDto personReadDto = personService.getById(id);
        log.info("Person info provided for person id = {}", id);
        return ResponseEntity.ok().body(personReadDto);
    }

    @PostMapping("/register")
    //For non-authorized users
    @Operation(summary = "Зарегистрировать пользователя", description = "Позволяет зарегистрировать нового пользователя")
    public ResponseEntity<Long> registerNewPerson(@RequestBody PersonRegisterDto personRegisterDto) {
        log.info("Person registration requested");
        Long id = personService.register(personRegisterDto);
        log.info("New person registered with id = {}", id);
        return ResponseEntity.ok().body(id);
    }

/*    @PostMapping
    @PreAuthorize("hasAuthority('persons:write')")
    @Operation(summary = "Создать пользователя", description = "Позволяет создать нового пользователя")
    public ResponseEntity<Long> newPerson(@RequestBody PersonCreateDto person) {
        log.info("Person creation requested.");
        Long id = personService.add(person);
        log.info("Person created with id = {}", id);
        return ResponseEntity.ok().body(id);
    }*/

/*    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('persons:write')")
    @Operation(summary = "Обновить данные пользователя", description = "Позволяет обновить данные пользователя")
    public ResponseEntity updatePerson(@PathVariable @Parameter(description = "Идентификатор пользователя") long id,
                                       @RequestParam @Parameter(description = "Имя пользователя (опционально)") Optional<String> name,
                                       @RequestParam @Parameter(description = "Перечень ролей пользователя (опционально)") Optional<Set<PersonRole>> roles) {
        log.info("Person update requested for id = {}", id);
        personService.update(id, name, roles);
        log.info("Person update done for id = {}", id);
        return ResponseEntity.ok().build();
    }*/

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('persons:write')")
    @Operation(summary = "Обновить данные пользователя", description = "Позволяет обновить данные пользователя")
    public ResponseEntity updatePerson(@PathVariable @Parameter(description = "Идентификатор пользователя") long id,
                                       @RequestBody PersonUpdateDto personUpdateDto) {
        log.info(String.format("Person update requested for id = %d", id));
        personService.update(id, personUpdateDto);
        log.info(String.format("Person update done for id = %d", id));
        return ResponseEntity.ok().build();
    }


    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('persons:write')")
    @Operation(summary = "Удалить пользователя", description = "Позволяет удалить пользователя")
    public ResponseEntity deletePerson(@PathVariable @Parameter(description = "Идентификатор пользователя") long id) {
        log.info("Person deletion requested for id = {}", id);
        personService.delete(id);
        log.info("Person id = {} deleted", id);
        return ResponseEntity.ok().build();
    }

}
