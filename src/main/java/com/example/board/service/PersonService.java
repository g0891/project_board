package com.example.board.service;

import com.example.board.rest.dto.person.PersonCreateDto;
import com.example.board.rest.dto.person.PersonReadDto;
import com.example.board.rest.dto.person.PersonUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PersonService{
    PersonReadDto getById(long id) throws BoardAppIncorrectIdException;
    List<PersonReadDto> getAll();
    long add(PersonCreateDto person);
    void update(long id, PersonUpdateDto person) throws BoardAppIncorrectIdException;
    void delete(long id) throws BoardAppIncorrectIdException;
}
