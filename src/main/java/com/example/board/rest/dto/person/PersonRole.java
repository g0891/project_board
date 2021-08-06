package com.example.board.rest.dto.person;

import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Optional;

public enum PersonRole {
    CUSTOMER,
    AUTHOR,
    EXECUTOR;

    @JsonCreator
    public static PersonRole create(String providedRole) throws BoardAppIncorrectEnumException {
        if (providedRole == null) {
            throw new BoardAppIncorrectEnumException("NULL", PersonRole.class);
        }

        Optional<PersonRole> personRole = Arrays.stream(PersonRole.values())
                .filter( role -> role.name().equalsIgnoreCase(providedRole))
                .findFirst();

        return personRole.orElseThrow(
                () -> new BoardAppIncorrectEnumException(providedRole, PersonRole.class)
        );

    }
}
