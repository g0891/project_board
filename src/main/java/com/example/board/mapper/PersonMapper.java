package com.example.board.mapper;

import com.example.board.entity.person.PersonEntity;
import com.example.board.entity.role.PersonRole;
import com.example.board.entity.role.RoleEntity;
import com.example.board.repository.RoleRepository;
import com.example.board.rest.dto.person.PersonReadDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class PersonMapper {

    protected RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

/*
    // id, name, roles - mapped automatically
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "roles", source = "roles")
            @Mapping(target = "status", source = "status")
    })
*/
    public abstract PersonReadDto personEntityToPersonReadDto(PersonEntity personEntity);

    public abstract Set<PersonRole> roleEntitySetToPersonRoleSet(Set<RoleEntity> roleEntitySet);

    public PersonRole roleEntityToPersonRole(RoleEntity roleEntity) {
        return PersonRole.create(roleEntity.getName());
    }

    /*@Mappings({
            @Mapping(target = "id", expression = "java(null)"),
            @Mapping(target = "projectsWhereCustomer", ignore = true),
            @Mapping(target = "tasksWhereAuthor", ignore = true),
            @Mapping(target = "tasksWhereExecutor", ignore = true)
*//*
            // name, roles - mapped automatically
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "roles", source = "roles")
*//*
    })
    public abstract PersonEntity personCreateDtoToPersonEntity(PersonCreateDto personCreateDto);*/

    public RoleEntity personRoleToRoleEntity(PersonRole personRole) {
        return roleRepository.findByNameIgnoreCase(personRole.name()).orElseThrow(
                () -> new BoardAppIncorrectEnumException(personRole.name(), PersonRole.class)
        );
    }

    public abstract List<PersonReadDto> personEntityListToPersonReadDtoList(List<PersonEntity> personEntity);

}
