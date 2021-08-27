package com.example.board.rest.errorController.exception;

public class BoardAppExternalMicroserviceException extends BoardAppException {
    public BoardAppExternalMicroserviceException(String msg) {
        super(msg);
    }

    @Override
    public Object[] getParams() {
        return null;
    }
}
