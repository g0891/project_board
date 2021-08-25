package com.example.board.mapper;

import com.example.board.entity.person.PersonEntity;
import com.example.board.entity.person.PersonStatus;
import com.example.board.entity.role.PersonRole;
import com.example.board.entity.role.RoleEntity;
import com.example.board.repository.RoleRepository;
import com.example.board.rest.dto.person.PersonReadDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonMapperTest {

    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private PersonMapperImpl personMapper;

    @Test
    void personEntityToPersonReadDto() {
        //given
        PersonEntity personEntity = new PersonEntity(1L,"John", null, PersonStatus.ACTIVE, Set.of(new RoleEntity(1, PersonRole.AUTHOR.name()), new RoleEntity(2, PersonRole.CUSTOMER.name())), null,null,null);

        //when
//        doReturn(Optional.of(new RoleEntity(1, PersonRole.CUSTOMER.name()))).when(roleRepository).findByNameIgnoreCase(PersonRole.CUSTOMER.name());
//        when(roleRepository.findByNameIgnoreCase(PersonRole.CUSTOMER.name()))
//                .thenReturn(Optional.of(new RoleEntity(1, PersonRole.CUSTOMER.name())));
//        when(roleRepository.findByNameIgnoreCase(PersonRole.AUTHOR.name())).thenReturn(Optional.of(new RoleEntity(2, PersonRole.AUTHOR.name())));

        //then
        PersonReadDto personReadDto = personMapper.personEntityToPersonReadDto(personEntity);
        assertEquals(1L, personReadDto.getId());
        assertEquals("John", personReadDto.getName());
        assertEquals(PersonStatus.ACTIVE, personReadDto.getStatus());
        assertEquals(2, personReadDto.getRoles().size());
        assertTrue(personReadDto.getRoles().contains(PersonRole.CUSTOMER));
        assertTrue(personReadDto.getRoles().contains(PersonRole.AUTHOR));

    }

    @Test
    void roleEntitySetToPersonRoleSet() {
        //Given
        RoleEntity roleEntity1 = new RoleEntity(1,"CUSTOMER");
        RoleEntity roleEntity2 = new RoleEntity(2,"AUTHOR");
        Set<RoleEntity> roleEntitySet = Set.of(roleEntity1, roleEntity2);

        //Then
        Set<PersonRole> personRoleSet = personMapper.roleEntitySetToPersonRoleSet(roleEntitySet);
        assertEquals(2, personRoleSet.size());
        assertTrue(personRoleSet.containsAll(Set.of(PersonRole.CUSTOMER, PersonRole.AUTHOR)));
    }

    @Test
    void roleEntityToPersonRole() {
        //given
        RoleEntity roleEntity1 = new RoleEntity(1,"CUSTOMER");
        RoleEntity roleEntity2 = new RoleEntity(2,"AUTHOR");
        RoleEntity roleEntity3 = new RoleEntity(3,"EXECUTOR");
        RoleEntity roleEntity4 = new RoleEntity(2,"ADMIN");

        //then
        assertEquals(PersonRole.CUSTOMER, personMapper.roleEntityToPersonRole(roleEntity1));
        assertEquals(PersonRole.AUTHOR, personMapper.roleEntityToPersonRole(roleEntity2));
        assertEquals(PersonRole.EXECUTOR, personMapper.roleEntityToPersonRole(roleEntity3));
        assertEquals(PersonRole.ADMIN, personMapper.roleEntityToPersonRole(roleEntity4));
    }

    @Test
    void personRoleToRoleEntity() {
        //Given
        //When
        when(roleRepository.findByNameIgnoreCase("AUTHOR")).thenReturn(Optional.of(new RoleEntity(1, "AUTHOR")));
        when(roleRepository.findByNameIgnoreCase("CUSTOMER")).thenReturn(Optional.empty());
        //Then
        RoleEntity roleEntity = personMapper.personRoleToRoleEntity(PersonRole.AUTHOR);
        assertEquals(1, roleEntity.getId());
        assertThrows(BoardAppIncorrectEnumException.class, () -> personMapper.personRoleToRoleEntity(PersonRole.CUSTOMER));
    }

    @Test
    void personEntityListToPersonReadDtoList() {
        //Given
        PersonEntity personEntity1 = new PersonEntity(1L,"John", null, PersonStatus.ACTIVE, Set.of(new RoleEntity(1, PersonRole.AUTHOR.name()), new RoleEntity(2, PersonRole.CUSTOMER.name())), null,null,null);
        PersonEntity personEntity2 = new PersonEntity(2L,"Ann", null, PersonStatus.ACTIVE, Set.of(new RoleEntity(2, PersonRole.EXECUTOR.name())), null,null,null);
        List<PersonEntity> personEntityList = List.of(personEntity1, personEntity2);

        //Then
        List<PersonReadDto> personReadDtoList = personMapper.personEntityListToPersonReadDtoList(personEntityList);
        assertEquals(personEntityList.size(), personReadDtoList.size());
        Optional<PersonReadDto> personDto1 = personReadDtoList.stream().filter(dto -> "John".equals(dto.getName())).findAny();
        Optional<PersonReadDto> personDto2 = personReadDtoList.stream().filter(dto -> "Ann".equals(dto.getName())).findAny();
        assertTrue(personDto1.isPresent());
        assertTrue(personDto2.isPresent());
        assertEquals(1L, personDto1.get().getId());
        assertEquals(PersonStatus.ACTIVE, personDto1.get().getStatus());
        assertTrue(personDto1.get().getRoles().containsAll(Set.of(PersonRole.CUSTOMER, PersonRole.AUTHOR)));
    }
}