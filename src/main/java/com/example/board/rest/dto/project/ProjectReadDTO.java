package com.example.board.rest.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Описание проекта для ответов")
public class ProjectReadDTO {
    @Schema(description = "Идентификатор проекта")
    private int id;
    @Schema(description = "Название проекта")
    private String name;
    @Schema(description = "Описание проекта")
    private String description;
    @Schema(description = "Идентификатор клиента")
    private int customerId;
    @Schema(description = "Статус проекта")
    private ProjectStatus status;

    public ProjectReadDTO(int id, String name, String description, int customerId, ProjectStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.customerId = customerId;
        this.status = status;
    }

    public ProjectReadDTO(ProjectCreateDTO req, int id) {
        this.id = id;
        this.status = ProjectStatus.OPEN;
        this.name = req.getName();
        this.description = req.getDescription();
        this.customerId = req.getCustomerId();
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
