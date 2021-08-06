package com.example.board.mapper;

import com.example.board.entity.ProjectEntity;
import com.example.board.entity.ReleaseEntity;
import com.example.board.repository.ProjectRepository;
import com.example.board.rest.dto.release.ReleaseCreateDto;
import com.example.board.rest.dto.release.ReleaseReadDto;
import com.example.board.rest.dto.release.ReleaseStatus;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", imports = {ReleaseStatus.class, LocalDateTime.class})
public abstract class ReleaseMapper {

    protected ProjectRepository projectRepository;

    @Autowired
    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    @Mappings({
            @Mapping(target = "projectId", expression = "java(releaseEntity.getProject().getId())"),
/*
            // id, version, status, createdOn, releasedOn - mapped automatically
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "version", source = "version"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "createdOn", source = "createdOn"),
            @Mapping(target = "releasedOn", source = "releasedOn")
*/
    })
    public abstract ReleaseReadDto releaseEntityToReleaseReadDto(ReleaseEntity releaseEntity);

    @Mappings({
            @Mapping(target = "id", expression = "java(null)"),
            @Mapping(target = "status", expression = "java(ReleaseStatus.OPEN)"),
            @Mapping(target = "createdOn", expression = "java(LocalDateTime.now())"),
            @Mapping(target = "releasedOn", expression = "java(null)"),
            @Mapping(target = "project", source = "projectId"),
            @Mapping(target = "tasks", ignore = true)
/*
            // version - mapped automatically
            @Mapping(target = "version", source = "version"),
*/
    })
    public abstract ReleaseEntity releaseCreateDtoToReleaseEntity(ReleaseCreateDto releaseCreateDto);

    public ProjectEntity projectIdToProjectEntity(long projectId) throws BoardAppIncorrectIdException {
        return projectRepository.findById(projectId).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no project with id = %d", projectId))
        );
    }

    public abstract List<ReleaseReadDto> releaseEntityListToReleaseReadDtoList(List<ReleaseEntity> releaseEntities);

}
