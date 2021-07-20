package com.example.board.rest.dto.release;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Описание релиза для создания")
public class ReleaseCreateDTO {
        @Schema(description = "Версия релиза")
        private String version;
        @Schema(description = "Идентификатор проекта")
        private int projectId;

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
}
