package com.example.board.rest.dto.release;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Описание релиза для обновления")
public class ReleaseUpdateDTO {
    @Schema(description = "Версия релиза")
    private String version;
    @Schema(description = "Идентификатор проекта")
    private int projectId;
    @Schema(description = "Статус релиза")
    private ReleaseStatus status;

    public ReleaseUpdateDTO(String version, int projectId, ReleaseStatus status) {
        this.version = version;
        this.projectId = projectId;
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public ReleaseStatus getStatus() {
        return status;
    }

    public void setStatus(ReleaseStatus status) {
        this.status = status;
    }
}