package com.example.board.rest.errorController.exception;

public class BoardAppIncorrectIdException extends BoardAppException{
    long id;
    public BoardAppIncorrectIdException(String msg) {
        super(msg);
    }
    public BoardAppIncorrectIdException(String msg, long id) {
        super(msg);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public Object[] getParams(){
        return new Object[]{id};
    }
}

