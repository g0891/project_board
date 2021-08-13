package com.example.board.rest.errorController.exception;

public class BoardAppExternalMicroserviceException extends RuntimeException {
    public BoardAppExternalMicroserviceException(String msg) {
        super(msg);
    }
}
