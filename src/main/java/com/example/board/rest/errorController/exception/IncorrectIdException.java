package com.example.board.rest.errorController.exception;

public class IncorrectIdException extends java.lang.Exception{
    public IncorrectIdException() {
        super("Id не может быть меньше или равен нулю");
    }
}

