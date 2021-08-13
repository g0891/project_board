package com.example.board.rest.dto.feign;

public class ErrorDto {
    private String message;

    public ErrorDto(String message) {
        this.message = message;
    }

    public ErrorDto() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
