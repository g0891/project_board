package com.example.board.mapper;

import com.example.board.entity.PersonEntity;
import com.example.board.entity.RoleEntity;
import com.example.board.repository.RoleRepository;
import com.example.board.rest.dto.person.PersonCreateDto;
import com.example.board.rest.dto.person.PersonReadDto;
import com.example.board.rest.dto.person.PersonRole;
import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class PersonMapper {

    @Autowired
    RoleRepository roleRepository;

/*
    // id, name, roles - mapped automatically
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "roles", source = "roles")
    })
*/
    public abstract PersonReadDto personEntityToPersonReadDto(PersonEntity personEntity);

    public abstract Set<PersonRole> roleEntitySetToPersonRoleSet(Set<RoleEntity> roleEntitySet);

    public PersonRole roleEntityToPersonRole(RoleEntity roleEntity) {
        return PersonRole.fromText(roleEntity.getName());
    }

    @Mappings({
            @Mapping(target = "id", expression = "java(null)")
/*
            // name, roles - mapped automatically
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "roles", source = "roles")
*/
    })
    public abstract PersonEntity personCreateDtoToPersonEntity(PersonCreateDto personCreateDto);

    public RoleEntity personRoleToRoleEntity(PersonRole personRole) {
        return roleRepository.findByNameIgnoreCase(personRole.getName()).orElseThrow(
                () -> new BoardAppIncorrectEnumException(personRole.getName(), PersonRole.class)
        );
    }

    public abstract List<PersonReadDto> personEntityListToPersonReadDtoList(List<PersonEntity> personEntity);

}