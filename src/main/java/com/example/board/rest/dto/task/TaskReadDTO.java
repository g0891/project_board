package com.example.board.rest.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Описание задачи для ответов")
public class TaskReadDTO {
    @Schema(description = "Идентификатор задачи")
    private int id;
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
    @Schema(description = "Дата создания")
    private LocalDateTime createdOn;
    @Schema(description = "Дата завершения")
    private LocalDateTime doneOn;

    public TaskReadDTO(int id, String name, String description, TaskStatus status, int authorId, int executorId, int releaseId, LocalDateTime createdOn, LocalDateTime doneOn) {
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

    public TaskReadDTO(int id, TaskCreateDTO task) {
        this.id = id;
        this.name = task.getName();
        this.description = task.getDescription();
        this.status = TaskStatus.BACKLOG;
        this.authorId = task.getAuthorId();
        this.executorId = task.getExecutorId();
        this.releaseId = task.getReleaseId();
        this.createdOn = LocalDateTime.now();
        this.doneOn = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
