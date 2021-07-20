package com.example.board.rest.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Описание проекта для обновления")
public class ProjectUpdateDTO {
    @Schema(description = "Название проекта")
    private String name;
    @Schema(description = "Описание проекта")
    private String description;
    @Schema(description = "Идентификатор клиента")
    private int customerId;
    @Schema(description = "Статус проекта")
    private ProjectStatus status;

    public ProjectUpdateDTO(String name, String description, int customerId, ProjectStatus status) {
        this.name = name;
        this.description = description;
        this.customerId = customerId;
        this.status = status;
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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus projectStatus) {
        this.status = projectStatus;
    }
}