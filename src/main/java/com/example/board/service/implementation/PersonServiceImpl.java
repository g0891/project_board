package com.example.board.service.implementation;

import com.example.board.entity.PersonEntity;
import com.example.board.entity.RoleEntity;
import com.example.board.mapper.PersonMapper;
import com.example.board.repository.PersonRepository;
import com.example.board.repository.RoleRepository;
import com.example.board.rest.dto.person.PersonCreateDto;
import com.example.board.rest.dto.person.PersonReadDto;
import com.example.board.rest.dto.person.PersonUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectRoleException;
import com.example.board.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public PersonReadDto getById(long id) throws BoardAppIncorrectIdException {
        PersonEntity personEntity = personRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("Person with Id = %d  not found.", id))
        );

        return personMapper.personEntityToPersonReadDto(personEntity);

    /*    return new PersonReadDto(
                personEntity.getId(),
                personEntity.getName(),
                personEntity.getRoles().stream().map(role -> PersonRole.valueOf(role.getName().toUpperCase())).collect(Collectors.toSet())
        );*/
    }

    @Override
    public List<PersonReadDto> getAll() {
        List<PersonEntity> personEntities = personRepository.findAll();
        return personMapper.personEntityListToPersonReadDtoList(personEntities);
        /*return personEntities.stream().map(person -> new PersonReadDto(
           person.getId(),
           person.getName(),
           person.getRoles()
                   .stream()
                   .map(RoleEntity::getName)
                   .map(String::toUpperCase)
                   .map(PersonRole::valueOf)
                   .collect(Collectors.toSet())
        )).collect(Collectors.toList());*/
    }

    @Override
    public long add(PersonCreateDto person) {
        PersonEntity personEntity = personMapper.personCreateDtoToPersonEntity(person);
        personEntity = personRepository.save(personEntity);
        return personEntity.getId();

        /*return 0;*/
    }

    @Override
    public void update(long id, PersonUpdateDto person) throws BoardAppIncorrectIdException {
        PersonEntity personEntity = personRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("Person with Id = %d  not found.", id))
        );

        Set<RoleEntity> roleEntities = new HashSet<>();
        person.getRoles().stream().forEach(role -> roleEntities.add(
                roleRepository.findByNameIgnoreCase(role.getName()).orElseThrow(
                    () -> new BoardAppIncorrectRoleException("Incorrect role name: " + role.getName())
        )));

        personEntity.setName(person.getName());
        personEntity.setRoles(roleEntities);
        personRepository.save(personEntity);

    }

    @Override
    public void delete(long id) throws BoardAppIncorrectIdException {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
        } else {
            throw new BoardAppIncorrectIdException(String.format("Person with Id = %d  not found.", id));
        }
    }
}
