package com.example.board.rest.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Описание задачи для обновления")
public class TaskUpdateDTO {
    @Schema(description = "Название задачи")
    private String name;
    @Schema(description = "Описание задачи")
    private String description;
    @Schema(description = "Статус задачи")
    private TaskStatus status;
    @Schema(description = "Автор")
    private int authorId;
    @Schema(description = "Исполнитель")
    private int executorId;
    @Schema(description = "Идентификатор релиза")
    private int releaseId;

    public TaskUpdateDTO(String name, String description, TaskStatus status, int authorId, int executorId, int releaseId) {
        this.name = name;
        this.description = description;
        this.status = status;
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

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
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