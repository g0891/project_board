package com.example.board.rest.dto.release;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Описание релиза для создания")
public class ReleaseCreateDto {
        @Schema(description = "Версия релиза")
        private String version;
        @Schema(description = "Идентификатор проекта")
        private long projectId;

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
}
