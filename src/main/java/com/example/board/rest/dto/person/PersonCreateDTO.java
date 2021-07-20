package com.example.board.rest.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Описание пользователя для создания")
public class PersonCreateDTO {
    @Schema(description = "Имя пользователя")
    private String name;
    @Schema(description = "Список ролей пользователя пользователя")
    private List<PersonRole> roles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PersonRole> getRoles() {
        return roles;
    }

    public void setRoles(List<PersonRole> roles) {
        this.roles = roles;
    }
}
