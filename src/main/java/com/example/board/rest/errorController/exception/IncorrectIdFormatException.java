package com.example.board.rest.errorController.exception;

public class IncorrectIdFormatException extends IncorrectIdException{
    public IncorrectIdFormatException() {
        super("Id не может быть меньше или равен нулю");
    }
}
