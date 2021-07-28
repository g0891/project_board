package com.example.board.rest.dto.person;

import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;

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


        for (PersonRole personRole: values()) {
            if (personRole.name().equalsIgnoreCase(text)) {
                return personRole;
            }
        }

        throw new BoardAppIncorrectEnumException(text, PersonRole.class);
    }
}
