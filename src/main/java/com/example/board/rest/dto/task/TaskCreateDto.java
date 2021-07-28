package com.example.board.rest.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Описание задачи для создания")
public class TaskCreateDto {
    @Schema(description = "Название задачи")
    private String name;
    @Schema(description = "Описание задачи")
    private String description;
    @Schema(description = "Автор")
    private long authorId;
    @Schema(description = "Исполнитель")
    private long executorId;
    @Schema(description = "Идентификатор релиза")
    private long releaseId;

    public TaskCreateDto() {
    }

    public TaskCreateDto(String name, String description, long authorId, long executorId, long releaseId) {
        this.name = name;
        this.description = description;
        this.authorId = authorId;
        this.executorId = executorId;
        this.releaseId = releaseId;
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

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(long executorId) {
        this.executorId = executorId;
    }

    public long getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(long releaseId) {
        this.releaseId = releaseId;
    }
}