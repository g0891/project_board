package com.example.board.rest.errorController.exception;

public abstract class BoardAppException extends RuntimeException{

    public BoardAppException(String message) {
        super(message);
    }

    public BoardAppException(String message, Throwable cause) {
        super(message,cause);
    }

    abstract public Object[] getParams();

}
