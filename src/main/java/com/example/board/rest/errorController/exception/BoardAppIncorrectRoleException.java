package com.example.board.rest.errorController.exception;

public class BoardAppIncorrectRoleException extends RuntimeException{
    public BoardAppIncorrectRoleException(String msg) {
        super(msg);
    }
}
