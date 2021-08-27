package com.example.board.rest.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Описание проекта для создания")
public class ProjectCreateDto {
    @Schema(description = "Название проекта")
    private String name;
    @Schema(description = "Описание проекта")
    private String description;
    @Schema(description = "Идентификатор клиента")
    private long customerId;

    public ProjectCreateDto() {
    }

    public ProjectCreateDto(String name, String description, long customerId) {
        this.name = name;
        this.description = description;
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

}