package com.example.board.rest.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Описание нового пользователя")
public class PersonRegisterDto {
    @Schema(description = "Имя пользователя (оно же логин)")
    private String name;
    @Schema(description = "Пароль")
    private String password;

    public PersonRegisterDto(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
