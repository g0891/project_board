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
    @Schema(description = "Стоимость проекта")
    private Long cost;

    public static class Builder{
        private String name;
        private String description;
        private Long customerId;
        private ProjectStatus status;
        private Long cost;

        public Builder addName(String name){
            this.name = name;
            return this;
        }
        public Builder addDescription(String description){
            this.description = description;
            return this;
        }
        public Builder addCustomerId(Long customerId){
            this.customerId = customerId;
            return this;
        }
        public Builder addStatus(ProjectStatus status){
            this.status = status;
            return this;
        }
        public Builder addCost(Long cost){
            this.cost = cost;
            return this;
        }

        public ProjectUpdateDto build(){
            return new ProjectUpdateDto(name,description,customerId,status,cost);
        }
    }

    public ProjectUpdateDto() {
    }

    public ProjectUpdateDto(String name, String description, Long customerId, ProjectStatus status, Long cost) {
        this.name = name;
        this.description = description;
        this.customerId = customerId;
        this.status = status;
        this.cost = cost;
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

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }
}