package com.example.board.rest.errorController.exception;

public class BoardAppConsistencyViolationException extends RuntimeException{
    public BoardAppConsistencyViolationException(String msg) {
        super(msg);
    }
}
