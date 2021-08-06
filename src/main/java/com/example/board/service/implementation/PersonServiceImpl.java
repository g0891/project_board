package com.example.board.service.implementation;

import com.example.board.entity.PersonEntity;
import com.example.board.entity.RoleEntity;
import com.example.board.mapper.PersonMapper;
import com.example.board.repository.PersonRepository;
import com.example.board.repository.RoleRepository;
import com.example.board.rest.dto.person.PersonCreateDto;
import com.example.board.rest.dto.person.PersonReadDto;
import com.example.board.rest.dto.person.PersonRole;
//import com.example.board.rest.dto.person.PersonUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppConsistencyViolationException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectRoleException;
import com.example.board.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final RoleRepository roleRepository;

    //@Autowired
    public PersonServiceImpl(PersonRepository personRepository, PersonMapper personMapper, RoleRepository roleRepository) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
        this.roleRepository = roleRepository;
    }

    @Override
    public PersonReadDto getById(long id) throws BoardAppIncorrectIdException {
        PersonEntity personEntity = personRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("Person with Id = %d  not found.", id))
        );
        return personMapper.personEntityToPersonReadDto(personEntity);
    }

    @Override
    public List<PersonReadDto> getAll() {
        List<PersonEntity> personEntities = personRepository.findAll();
        return personMapper.personEntityListToPersonReadDtoList(personEntities);
    }

    @Override
    public long add(PersonCreateDto person) {
        PersonEntity personEntity = personMapper.personCreateDtoToPersonEntity(person);
        personEntity = personRepository.save(personEntity);
        return personEntity.getId();

    }

    @Override
    public void update(long id, Optional<String> name, Optional<Set<PersonRole>> roles) {
        PersonEntity personEntity = personRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("Person with Id = %d  not found.", id))
        );

        name.ifPresent(personEntity::setName);

        if (roles.isPresent()) {
            Set<RoleEntity> roleEntities = new HashSet<>();
            roles.get().stream().forEach(role -> roleEntities.add(
                    roleRepository.findByNameIgnoreCase(role.name()).orElseThrow(
                            () -> new BoardAppIncorrectRoleException("Incorrect role name: " + role.name())
                    )));
            personEntity.setRoles(roleEntities);
        }

        personRepository.save(personEntity);

    }

    @Override
    public void delete(long id) throws BoardAppIncorrectIdException, BoardAppConsistencyViolationException {
        PersonEntity personEntity = personRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("Person with Id = %d  not found.", id))
        );
        if (!personEntity.getProjectsWhereCustomer().isEmpty()) {
            throw new BoardAppConsistencyViolationException("A person can't be deleted because of participating in projects as CUSTOMER");
        }
        if (!personEntity.getTasksWhereAuthor().isEmpty()) {
            throw new BoardAppConsistencyViolationException("A person can't be deleted because of being an AUTHOR of some tasks.");
        }
        if (!personEntity.getTasksWhereExecutor().isEmpty()) {
            throw new BoardAppConsistencyViolationException("A person can't be deleted because of being an EXECUTOR in some tasks.");
        }

        personRepository.deleteById(id);
    }
}
