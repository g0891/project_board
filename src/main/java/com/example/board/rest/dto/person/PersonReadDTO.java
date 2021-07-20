package com.example.board.rest.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Описание пользователя для ответов")
public class PersonReadDTO {
    @Schema(description = "Идентификатор пользователя")
    private int id;
    @Schema(description = "Имя пользователя")
    private String name;
    @Schema(description = "Список ролей пользователя пользователя")
    private List<PersonRole> roles;

    public PersonReadDTO(int id, String name, List<PersonRole> roles) {
        this.id = id;
        this.name = name;
        this.roles = roles;
    }

    public PersonReadDTO(int id, PersonCreateDTO person) {
        this.id = id;
        this.name = person.getName();
        this.roles = person.getRoles();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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