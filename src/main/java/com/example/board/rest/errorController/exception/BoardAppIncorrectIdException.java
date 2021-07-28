package com.example.board.rest.errorController.exception;

public class BoardAppIncorrectIdException extends RuntimeException{
    public BoardAppIncorrectIdException(String msg) {
        super(msg);
    }
}

