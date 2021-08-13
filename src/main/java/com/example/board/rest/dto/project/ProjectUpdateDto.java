package com.example.board.rest.dto.project;

import com.example.board.entity.project.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Описание проекта для обновления")
public class ProjectUpdateDto {
    @Schema(description = "Название проекта")
    private String name;
    @Schema(description = "Описание проекта")
    private String description;
    @Schema(description = "Идентификатор клиента")
    private Long customerId;
    @Schema(description = "Статус проекта")
    private ProjectStatus status;

    public ProjectUpdateDto() {
    }

    public ProjectUpdateDto(String name, String description, Long customerId, ProjectStatus status) {
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus projectStatus) {
        this.status = projectStatus;
    }
}