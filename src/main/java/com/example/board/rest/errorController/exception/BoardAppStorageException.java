package com.example.board.rest.errorController.exception;

public class BoardAppStorageException extends RuntimeException{
    public BoardAppStorageException(String msg) {
        super(msg);
    }

    public BoardAppStorageException(String msg, Throwable e){
        super(msg, e);
    }
}
