package com.example.board.rest.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Описание пользователя для ответов")
public class PersonReadDto {
    @Schema(description = "Идентификатор пользователя")
    private long id;
    @Schema(description = "Имя пользователя")
    private String name;
    @Schema(description = "Список ролей пользователя пользователя")
    private List<PersonRole> roles;

    public PersonReadDto(long id, String name, List<PersonRole> roles) {
        this.id = id;
        this.name = name;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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