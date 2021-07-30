package com.example.board.rest.dto.person;

import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;

import java.util.Arrays;
import java.util.Optional;

public enum PersonRole {
    CUSTOMER("Customer"),
    AUTHOR("Author"),
    EXECUTOR("Executor");

    private final String name;

    PersonRole(String text) {
        this.name = text;
    }

    public String getName(){
        return name;
    }

    public static PersonRole fromText(String text) {
        if (text == null) {
            throw new BoardAppIncorrectEnumException("NULL", PersonRole.class);
        }

        Optional<PersonRole> personRole = Arrays.stream(PersonRole.values())
                .filter( role -> role.name().equalsIgnoreCase(text))
                .findFirst();

        if (personRole.isPresent()) {
            return personRole.get();
        } else {
            throw new BoardAppIncorrectEnumException(text, PersonRole.class);
        }
    }
}
