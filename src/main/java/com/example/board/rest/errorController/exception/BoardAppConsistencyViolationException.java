package com.example.board.rest.errorController.exception;

public class BoardAppConsistencyViolationException extends BoardAppException{
    private long id;
    public BoardAppConsistencyViolationException(String msg, long id) {
        super(msg);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public Object[] getParams() {
        return new Object[]{id};
    }
}
