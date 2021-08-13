package com.example.board.service;

//import com.example.board.rest.dto.person.PersonCreateDto;

import com.example.board.rest.dto.person.PersonReadDto;
import com.example.board.rest.dto.person.PersonRegisterDto;
import com.example.board.rest.dto.person.PersonUpdateDto;

import java.util.List;

/**
 * Interface for working with persons.
 */
public interface PersonService{
    /**
     * Searches and returns information about the person (if found).
     * @param id An id of person to search for
     * @return A person description
     */
    PersonReadDto getById(long id);

    /**
     * Retrieving a complete list of persons.
     * @return A list of registered persons
     */
    List<PersonReadDto> getAll();

    //long add(PersonCreateDto person);

    /**
     * Used to register a new person in the system.
     * @param personRegisterDto A DTO with parameters required to register a new person
     * @return Id of a newly registered person
     */
    long register(PersonRegisterDto personRegisterDto);

    /*void update(long id,
                Optional<String> name,
                Optional<Set<PersonRole>> roles);*/

    /**
     * Used to update person information. E.g. name or roles.
     * @param id An id of person to search for
     * @param personUpdateDto A DTO with parameters required to update person info
     */
    void update(long id,
                PersonUpdateDto personUpdateDto);

    /**
     * Deletes a person from the system.
     * @param id An id of person to search for
     */
    void delete(long id);
}
