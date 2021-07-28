package com.example.board.rest.dto.project;

import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProjectStatus {
    OPEN("Open"),
    CLOSED("Closed");

    private final String status;

    ProjectStatus(String status) {
        this.status = status;
    }

    @JsonCreator
    public static ProjectStatus create(String providedStatus) throws BoardAppIncorrectEnumException {
        if (providedStatus == null) {
            throw new BoardAppIncorrectEnumException("NULL", ProjectStatus.class);
        }
        for (ProjectStatus s: ProjectStatus.values()) {
            if (s.name().equalsIgnoreCase(providedStatus)) {
                return s;
            }
        }
        throw new BoardAppIncorrectEnumException(providedStatus, ProjectStatus.class);
    }
}
