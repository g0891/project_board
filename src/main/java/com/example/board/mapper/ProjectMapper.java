package com.example.board.mapper;

import com.example.board.entity.PersonEntity;
import com.example.board.entity.ProjectEntity;
import com.example.board.repository.PersonRepository;
import com.example.board.rest.dto.project.ProjectCreateDto;
import com.example.board.rest.dto.project.ProjectReadDto;
import com.example.board.rest.dto.project.ProjectStatus;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", imports = {ProjectStatus.class})
public abstract class ProjectMapper {
/*    @Autowired
    PersonRepository personRepository;*/

    protected PersonRepository personRepository;

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @Mappings({
            @Mapping(target = "id", expression = "java(null)"),
            @Mapping(target = "status", expression = "java(ProjectStatus.OPEN)"),
            @Mapping(target = "customer", source = "customerId")
/*
            // name, description - mapped automatically
            @Mapping(target = "name", source = "project.name"),
            @Mapping(target = "description", source = "project.description"),
*/
    })
    public abstract ProjectEntity projectCreateDtoToProjectEntity(ProjectCreateDto project);

    @Mappings({
            @Mapping(target = "customerId", expression = "java(entity.getCustomer().getId())")
/*
            // id, name, description, status - mapped automatically
            @Mapping(target = "id", source = "entity.id"),
            @Mapping(target = "name", source = "entity.name"),
            @Mapping(target = "description", source = "entity.description"),
            @Mapping(target = "status", source = "entity.status")
*/
    })
    public abstract ProjectReadDto projectEntityToProjectReadDto(ProjectEntity entity);

    public PersonEntity customerIdToCustomer(long id) throws BoardAppIncorrectIdException {
        return personRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no customer with id = %d", id))
        );
    }

    public abstract List<ProjectReadDto> projectEntityListToProjectReadDtoList(List<ProjectEntity> list);
}
