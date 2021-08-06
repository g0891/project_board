package com.example.board.rest.dto.person;

import com.example.board.entity.person.PersonStatus;
import com.example.board.entity.role.PersonRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Set;

@Schema(description = "Описание пользователя для обновления")
public class PersonUpdateDto {
    @Schema(description = "Имя пользователя")
    private String name;
    @Schema(description = "Список ролей пользователя пользователя")
    private Set<PersonRole> roles;
    @Schema(description = "Статус пользователя")
    private PersonStatus status;

    public PersonUpdateDto() {
    }

    public PersonUpdateDto(String name, Set<PersonRole> roles, PersonStatus status) {
        this.name = name;
        this.roles = roles;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PersonRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<PersonRole> roles) {
        this.roles = roles;
    }

    public PersonStatus getStatus() {
        return status;
    }

    public void setStatus(PersonStatus status) {
        this.status = status;
    }
}