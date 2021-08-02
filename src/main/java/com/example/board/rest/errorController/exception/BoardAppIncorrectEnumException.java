package com.example.board.rest.errorController.exception;

import java.util.Arrays;

public class BoardAppIncorrectEnumException extends RuntimeException{
    public <E extends Enum<E>> BoardAppIncorrectEnumException(String providedValue, Class<E> enumClass) {
        super(String.format(
                "Provided value '%s' is incompatible with enumeration class %s containing values: %s",
                providedValue,
                enumClass.getSimpleName(),
                Arrays.toString(enumClass.getEnumConstants())
        ));
    }
}
