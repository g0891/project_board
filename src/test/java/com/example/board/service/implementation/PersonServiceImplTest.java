package com.example.board.service.implementation;

import com.example.board.entity.person.PersonEntity;
import com.example.board.entity.person.PersonStatus;
import com.example.board.entity.project.ProjectEntity;
import com.example.board.entity.role.PersonRole;
import com.example.board.entity.role.RoleEntity;
import com.example.board.entity.task.TaskEntity;
import com.example.board.mapper.PersonMapper;
import com.example.board.repository.PersonRepository;
import com.example.board.repository.RoleRepository;
import com.example.board.rest.dto.person.PersonReadDto;
import com.example.board.rest.dto.person.PersonRegisterDto;
import com.example.board.rest.dto.person.PersonUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppConsistencyViolationException;
import com.example.board.rest.errorController.exception.BoardAppIllegalArgumentException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectRoleException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest
class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonMapper personMapper;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private PersonServiceImpl personService;

    //Test instances
    private final PersonEntity personEntity1 = new PersonEntity(1L,"John",null, PersonStatus.ACTIVE, Set.of(new RoleEntity(1, "AUTHOR")), Set.of(), Set.of(), Set.of());
    private final PersonEntity personEntity2 = new PersonEntity(2L,"Ann",null, PersonStatus.ACTIVE, Set.of(new RoleEntity(2, "CUSTOMER")), Set.of(), Set.of(), Set.of());
    private final PersonReadDto personReadDto1 = new PersonReadDto(1L,"John", Set.of(PersonRole.AUTHOR), PersonStatus.ACTIVE);
    private final PersonReadDto personReadDto2 = new PersonReadDto(2L,"Ann", Set.of(PersonRole.CUSTOMER), PersonStatus.ACTIVE);

    @Captor
    ArgumentCaptor<PersonEntity> personEntityArgumentCaptor;

    @Test
    void getById() {
        //Given
        long existingPersonId = 1L;
        long notExistingPersonId = 2L;

        //When
        when(personRepository.findById(existingPersonId)).thenReturn(Optional.of(personEntity1));
        when(personRepository.findById(notExistingPersonId)).thenThrow(BoardAppIncorrectIdException.class);
        when(personMapper.personEntityToPersonReadDto(personEntity1)).thenReturn(personReadDto1);

        //Then
        assertEquals(personReadDto1, personService.getById(existingPersonId));
        assertThrows(BoardAppIncorrectIdException.class, () -> personService.getById(notExistingPersonId));
    }

    @Test
    void getAll() {
        //Given
        List<PersonEntity> personEntities = List.of(personEntity1, personEntity2);
        List<PersonReadDto> personReadDtos = List.of(personReadDto1, personReadDto2);

        //When
        when(personRepository.findAll()).thenReturn(personEntities);
        when(personMapper.personEntityListToPersonReadDtoList(personEntities)).thenReturn(personReadDtos);

        //Then
        List<PersonReadDto> actualPersonReadDtoList = personService.getAll();
        assertEquals(2, actualPersonReadDtoList.size());
        assertTrue(actualPersonReadDtoList.stream().map(PersonReadDto::getName).collect(Collectors.toSet()).containsAll(Set.of("John", "Ann")));

    }

    @Test
    void register() {
        //Given
        String encodedPassword = "encodedPassword";
        PersonRegisterDto validPersonRegisterDto = new PersonRegisterDto("John", "password");

        PersonRegisterDto[] invalidDtos = new PersonRegisterDto[]{
                null,
                new PersonRegisterDto(null ,"password"),
                new PersonRegisterDto("", "password"),
                new PersonRegisterDto(" ", "password"),
                new PersonRegisterDto("John", null),
                new PersonRegisterDto("John", ""),
                new PersonRegisterDto("John", " ")
        };

        //When
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(personRepository.save(any())).thenReturn(personEntity1);

        //Then
        Long registeredPersonId = personService.register(validPersonRegisterDto);
        assertEquals(1L, registeredPersonId);
        verify(personRepository).save(personEntityArgumentCaptor.capture());
        assertEquals(validPersonRegisterDto.getName(), personEntityArgumentCaptor.getValue().getName());
        assertEquals(encodedPassword, personEntityArgumentCaptor.getValue().getPassword());
        assertEquals(PersonStatus.ACTIVE, personEntityArgumentCaptor.getValue().getStatus());

        for (PersonRegisterDto invalidPersonRegisterDto: invalidDtos) {
            assertThrows(BoardAppIllegalArgumentException.class, () -> personService.register(invalidPersonRegisterDto));
        }
    }

    @Test
    void update() {
        //Given
        long notExistingId = 0L;
        PersonEntity alreadyExistingPerson1 = personEntity1;
        PersonEntity alreadyExistingPerson2 = personEntity2;
        PersonUpdateDto forAlreadyExistingPerson = new PersonUpdateDto(alreadyExistingPerson1.getName().intern(), Set.of(), null);


        //When
        when(personRepository.findById(notExistingId)).thenReturn(Optional.empty());
        when(personRepository.findById(alreadyExistingPerson1.getId())).thenReturn(Optional.of(alreadyExistingPerson1));
        when(personRepository.findByName(alreadyExistingPerson2.getName().intern())).thenReturn(Optional.of(alreadyExistingPerson2));
        when(roleRepository.findByNameIgnoreCase("CUSTOMER")).thenReturn(Optional.of(new RoleEntity(1,"AUTHOR")));
        //Suppose we forgot to add EXECUTOR role to DB
        when(roleRepository.findByNameIgnoreCase("EXECUTOR")).thenReturn(Optional.empty());
        when(personRepository.save(any())).thenReturn(null);

        //Then
        assertThrows(BoardAppIncorrectIdException.class, () -> personService.update(notExistingId,null));
        assertThrows(BoardAppIllegalArgumentException.class, () -> personService.update(alreadyExistingPerson1.getId(), new PersonUpdateDto("", null, null)));
        assertThrows(BoardAppIllegalArgumentException.class, () -> personService.update(alreadyExistingPerson1.getId(), new PersonUpdateDto(alreadyExistingPerson2.getName(), null, null)));
        assertThrows(BoardAppIncorrectRoleException.class, () -> personService.update(alreadyExistingPerson1.getId(), new PersonUpdateDto(null, Set.of(PersonRole.EXECUTOR), null)));
        personService.update(alreadyExistingPerson1.getId(), new PersonUpdateDto(null, Set.of(PersonRole.CUSTOMER), null));
        verify(personRepository,times(1)).save(any());

    }

    @Test
    void delete() {
        //Given
        long notExistingUserId = 0L;
        PersonEntity[] users = new PersonEntity[]{
                new PersonEntity(1L,"User1", null, null, Set.of(), Set.of(), Set.of(), Set.of()),
                new PersonEntity(2L,"User2", null, null, null, Set.of(new ProjectEntity()), Set.of(), Set.of()),
                new PersonEntity(3L,"User3", null, null, null, Set.of(), Set.of(new TaskEntity()), Set.of()),
                new PersonEntity(4L,"User4", null, null, null, Set.of(), Set.of(), Set.of(new TaskEntity()))
        };

        //When
        when(personRepository.findById(notExistingUserId)).thenThrow(BoardAppIncorrectIdException.class);
        when(personRepository.findById(1L)).thenReturn(Optional.of(users[0]));
        when(personRepository.findById(2L)).thenReturn(Optional.of(users[1]));
        when(personRepository.findById(3L)).thenReturn(Optional.of(users[2]));
        when(personRepository.findById(4L)).thenReturn(Optional.of(users[3]));
        doNothing().when(personRepository).deleteById(any());

        //Then
        assertThrows(BoardAppIncorrectIdException.class, () -> personService.delete(notExistingUserId));
        assertThrows(BoardAppConsistencyViolationException.class, () -> personService.delete(2L));
        assertThrows(BoardAppConsistencyViolationException.class, () -> personService.delete(3L));
        assertThrows(BoardAppConsistencyViolationException.class, () -> personService.delete(4L));
        personService.delete(1L);
        verify(personRepository,times(1)).deleteById(1L);
    }
}