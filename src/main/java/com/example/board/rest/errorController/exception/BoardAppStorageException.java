package com.example.board.rest.errorController.exception;

public class BoardAppStorageException extends BoardAppException{
    private String path;
    public BoardAppStorageException(String msg) {
        super(msg);
    }

    public BoardAppStorageException(String msg, Throwable e){
        super(msg, e);
    }

    public BoardAppStorageException(String msg, String path) {
        super(msg);
        this.path = path;
    }

    public BoardAppStorageException(String msg, String path, Throwable e) {
        super(msg,e);
        this.path = path;
    }

    @Override
    public Object[] getParams() {
        return new Object[]{path};
    }
}
