package com.example.board.rest.dto.release;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Описание релиза для ответов")
public class ReleaseReadDto {
        @Schema(description = "Идентификатор релиза")
        private long id;
        @Schema(description = "Версия релиза")
        private String version;
        @Schema(description = "Идентификатор проекта")
        private long projectId;
        @Schema(description = "Статус релиза")
        private ReleaseStatus status;
        @Schema(description = "Дата создания релиза")
        private LocalDateTime createdOn;
        @Schema(description = "Дата выпуска релиза")
        private LocalDateTime releasedOn;

    public ReleaseReadDto() {
    }

    public ReleaseReadDto(long id, String version, long projectId, ReleaseStatus status, LocalDateTime createdOn, LocalDateTime releasedOn) {
            this.id = id;
            this.version = version;
            this.projectId = projectId;
            this.status = status;
            this.createdOn = createdOn;
            this.releasedOn = releasedOn;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public long getProjectId() {
            return projectId;
        }

        public void setProjectId(long projectId) {
            this.projectId = projectId;
        }

        public ReleaseStatus getStatus() {
            return status;
        }

        public void setStatus(ReleaseStatus status) {
            this.status = status;
        }

        public LocalDateTime getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
        }

        public LocalDateTime getReleasedOn() {
            return releasedOn;
        }

        public void setReleasedOn(LocalDateTime releasedOn) {
            this.releasedOn = releasedOn;
        }
}
