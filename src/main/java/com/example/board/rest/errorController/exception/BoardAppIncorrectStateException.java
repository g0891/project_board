package com.example.board.rest.errorController.exception;

public class BoardAppIncorrectStateException extends RuntimeException {
    public BoardAppIncorrectStateException(String msg) {
        super(msg);
    }
}
