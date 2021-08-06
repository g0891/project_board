package com.example.board.service;

//import com.example.board.rest.dto.person.PersonCreateDto;
import com.example.board.rest.dto.person.PersonReadDto;
import com.example.board.entity.role.PersonRole;
import com.example.board.rest.dto.person.PersonRegisterDto;
import com.example.board.rest.dto.person.PersonUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PersonService{
    PersonReadDto getById(long id) throws BoardAppIncorrectIdException;
    List<PersonReadDto> getAll();
    //long add(PersonCreateDto person);
    long register(PersonRegisterDto personRegisterDto);
    /*void update(long id,
                Optional<String> name,
                Optional<Set<PersonRole>> roles);*/
    void update(long id,
                PersonUpdateDto personUpdateDto);
    void delete(long id) throws BoardAppIncorrectIdException;
}
