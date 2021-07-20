package com.example.board.rest.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Описание задачи для создания")
public class TaskCreateDTO {
    @Schema(description = "Название задачи")
    private String name;
    @Schema(description = "Описание задачи")
    private String description;
    @Schema(description = "Автор")
    private int authorId;
    @Schema(description = "Исполнитель")
    private int executorId;
    @Schema(description = "Идентификатор релиза")
    private int releaseId;

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

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getExecutorId() {
        return executorId;
    }

    public void setExecutorId(int executorId) {
        this.executorId = executorId;
    }

    public int getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(int releaseId) {
        this.releaseId = releaseId;
    }
}