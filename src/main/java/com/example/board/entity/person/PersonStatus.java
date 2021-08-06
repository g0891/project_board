package com.example.board.entity.person;

import com.example.board.entity.project.ProjectStatus;
import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Optional;

public enum PersonStatus {
    ACTIVE,
    DEACTIVATED;

    @JsonCreator
    public static PersonStatus create(String providedStatus) throws BoardAppIncorrectEnumException {
        if (providedStatus == null) {
            throw new BoardAppIncorrectEnumException("NULL", PersonStatus.class);
        }

        Optional<PersonStatus> personStatus = Arrays.stream(PersonStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(providedStatus))
                .findAny();

        return personStatus.orElseThrow(
                () -> new BoardAppIncorrectEnumException(providedStatus, PersonStatus.class)
        );

    }
}
