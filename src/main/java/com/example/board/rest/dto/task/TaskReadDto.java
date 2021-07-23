package com.example.board.rest.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Описание задачи для ответов")
public class TaskReadDto {
    @Schema(description = "Идентификатор задачи")
    private long id;
    @Schema(description = "Название задачи")
    private String name;
    @Schema(description = "Описание задачи")
    private String description;
    @Schema(description = "Статус задачи")
    private TaskStatus status;
    @Schema(description = "Автор")
    private long authorId;
    @Schema(description = "Исполнитель")
    private long executorId;
    @Schema(description = "Идентификатор релиза")
    private long releaseId;
    @Schema(description = "Дата создания")
    private LocalDateTime createdOn;
    @Schema(description = "Дата завершения")
    private LocalDateTime doneOn;

    public TaskReadDto(long id, String name, String description, TaskStatus status, long authorId, long executorId, long releaseId, LocalDateTime createdOn, LocalDateTime doneOn) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.authorId = authorId;
        this.executorId = executorId;
        this.releaseId = releaseId;
        this.createdOn = createdOn;
        this.doneOn = doneOn;
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

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
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

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getDoneOn() {
        return doneOn;
    }

    public void setDoneOn(LocalDateTime doneOn) {
        this.doneOn = doneOn;
    }
}
