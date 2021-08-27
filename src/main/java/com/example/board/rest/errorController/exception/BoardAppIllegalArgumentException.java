package com.example.board.rest.errorController.exception;

public class BoardAppIllegalArgumentException extends BoardAppException{
    public BoardAppIllegalArgumentException(String message) {
        super(message);
    }

    @Override
    public Object[] getParams() {
        return new Object[0];
    }
}
