package com.example.board.rest.errorController.exception;

public class IdNotFoundException extends IncorrectIdException{
    public IdNotFoundException() {
        super("An object not found for with provided Id");
    }
}
