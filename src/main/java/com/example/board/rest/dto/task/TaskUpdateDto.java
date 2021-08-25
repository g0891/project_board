package com.example.board.rest.dto.task;

import com.example.board.entity.task.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Описание задачи для обновления")
public class TaskUpdateDto {
    @Schema(description = "Название задачи")
    private String name;
    @Schema(description = "Описание задачи")
    private String description;
    @Schema(description = "Статус задачи")
    private TaskStatus status;
    @Schema(description = "Автор")
    private Long authorId;
    @Schema(description = "Исполнитель")
    private Long executorId;
    @Schema(description = "Идентификатор релиза")
    private Long releaseId;

    public static class Builder{
        private String name;
        private String description;
        private TaskStatus status;
        private Long authorId;
        private Long executorId;
        private Long releaseId;

        public Builder addName(String name){
            this.name = name;
            return this;
        }
        public Builder addDescription(String description){
            this.description = description;
            return this;
        }
        public Builder addStatus(TaskStatus status){
            this.status = status;
            return this;
        }
        public Builder addAuthorId(Long authorId){
            this.authorId = authorId;
            return this;
        }
        public Builder addExecutorId(Long executorId){
            this.executorId = executorId;
            return this;
        }
        public Builder addReleaseId(Long releaseId){
            this.releaseId = releaseId;
            return this;
        }

        public TaskUpdateDto build(){
            return new TaskUpdateDto(name,description,status,authorId,executorId,releaseId);
        }
    }

    public TaskUpdateDto() {
    }

    public TaskUpdateDto(String name, String description, TaskStatus status, Long authorId, Long executorId, Long releaseId) {
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

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public Long getExecutorId() {
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