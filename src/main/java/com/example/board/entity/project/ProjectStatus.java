package com.example.board.entity.project;

import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Optional;

public enum ProjectStatus {
    OPEN,
    CLOSED;

    @JsonCreator
    public static ProjectStatus create(String providedStatus) throws BoardAppIncorrectEnumException {
        if (providedStatus == null) {
            throw new BoardAppIncorrectEnumException("NULL", ProjectStatus.class);
        }

        Optional<ProjectStatus> projectStatus = Arrays.stream(ProjectStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(providedStatus))
                .findAny();

        return projectStatus.orElseThrow(
                () -> new BoardAppIncorrectEnumException(providedStatus, ProjectStatus.class)
        );

    }
}
