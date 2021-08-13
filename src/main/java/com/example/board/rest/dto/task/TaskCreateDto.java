package com.example.board.rest.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Описание задачи для создания")
public class TaskCreateDto {
    public static String[] fields = {"name", "description", "authorId", "executorId", "releaseId"};

    @Schema(description = "Название задачи")
    private String name;
    @Schema(description = "Описание задачи")
    private String description;
    @Schema(description = "Автор")
    private long authorId;
    @Schema(description = "Исполнитель (опционально)")
    private Long executorId;
    @Schema(description = "Идентификатор релиза")
    private long releaseId;

    public TaskCreateDto() {
    }

    public TaskCreateDto(String name, String description, long authorId, Long executorId, long releaseId) {
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

    public Long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
    }

    public long getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(long releaseId) {
        this.releaseId = releaseId;
    }
}