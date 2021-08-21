package com.example.board.rest.dto.project;

import com.example.board.entity.project.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Описание проекта для ответов")
public class ProjectReadDto {
    @Schema(description = "Идентификатор проекта")
    private long id;
    @Schema(description = "Название проекта")
    private String name;
    @Schema(description = "Описание проекта")
    private String description;
    @Schema(description = "Идентификатор клиента")
    private long customerId;
    @Schema(description = "Статус проекта")
    private ProjectStatus status;
    @Schema(description = "Стоимость проекта")
    private Long cost;

    public ProjectReadDto() {
    }

    public ProjectReadDto(long id, String name, String description, long customerId, ProjectStatus status, Long cost) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.customerId = customerId;
        this.status = status;
        this.cost = cost;
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

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus projectStatus) {
        this.status = projectStatus;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }
}
