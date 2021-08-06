package com.example.board.rest.dto.release;

import com.example.board.entity.release.ReleaseStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Описание релиза для обновления")
public class ReleaseUpdateDto {
    @Schema(description = "Версия релиза")
    private String version;
    @Schema(description = "Статус релиза")
    private ReleaseStatus status;

    public ReleaseUpdateDto() {
    }

    public ReleaseUpdateDto(String version, ReleaseStatus status) {
        this.version = version;
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ReleaseStatus getStatus() {
        return status;
    }

    public void setStatus(ReleaseStatus status) {
        this.status = status;
    }
}