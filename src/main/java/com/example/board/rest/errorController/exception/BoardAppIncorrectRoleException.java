package com.example.board.rest.errorController.exception;

public class BoardAppIncorrectRoleException extends BoardAppException{
    private String roleName;

    public BoardAppIncorrectRoleException(String message) {
        super(message);
    }

    public BoardAppIncorrectRoleException(String message, String roleName) {
        super(message);
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public Object[] getParams(){
        return new Object[]{roleName};
    }
}
