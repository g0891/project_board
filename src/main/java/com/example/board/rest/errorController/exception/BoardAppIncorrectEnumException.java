package com.example.board.rest.errorController.exception;

import java.util.Arrays;

public class BoardAppIncorrectEnumException extends BoardAppException{

    private String providedValue;
    private String simpleName;
    private String constants;

    public <E extends Enum<E>> BoardAppIncorrectEnumException(String providedValue, Class<E> enumClass) {
        super("BoardAppIncorrectEnumException.wrongEnumValue");
        this.providedValue = providedValue;
        this.simpleName = enumClass.getSimpleName();
        this.constants = Arrays.toString(enumClass.getEnumConstants());
    }

    @Override
    public Object[] getParams() {
        return new Object[]{providedValue,simpleName,constants};
    }
}
