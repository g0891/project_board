package com.example.board.rest.errorController.exception;

public class BoardAppIncorrectStateException extends BoardAppException {
    public BoardAppIncorrectStateException(String msg) {
        super(msg);
    }

    @Override
    public Object[] getParams() {
        return new Object[0];
    }
}
