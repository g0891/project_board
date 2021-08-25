package com.example.board.rest.dto.task;

import com.example.board.entity.task.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Описание задачи для поиска")
public class TaskSearchDto {
    @Schema(description = "Идентификатор задачи (опционально)")
    private Long id;
    @Schema(description = "Часть имени задачи (опционально)")
    private String name;
    @Schema(description = "Часть описания задачи (опционально)")
    private String description;
    @Schema(description = "Статус задачи (опционально)")
    private TaskStatus status;
    @Schema(description = "Идентификатор автора (опционально)")
    private Long authorId;
    @Schema(description = "Идентификатор исполнителя (опционально)")
    private Long executorId;
    @Schema(description = "Идентификатор релиза (опционально)")
    private Long releaseId;
    @Schema(description = "Идентификатор проекта (опционально)")
    private Long projectId;

    public static class Builder{
        private Long id;
        private String name;
        private String description;
        private TaskStatus status;
        private Long authorId;
        private Long executorId;
        private Long releaseId;
        private Long projectId;

        public Builder addId(Long id){
            this.id = id;
            return this;
        }
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
        public Builder addProjectId(Long projectId){
            this.projectId = projectId;
            return this;
        }
        public TaskSearchDto build() {
            return new TaskSearchDto(id,name,description,status,authorId,executorId,releaseId,projectId);
        }
    }

    public TaskSearchDto() {
    }

    public TaskSearchDto(Long id, String name, String description, TaskStatus status, Long authorId, Long executorId, Long releaseId, Long projectId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.authorId = authorId;
        this.executorId = executorId;
        this.releaseId = releaseId;
        this.projectId = projectId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
    }

    public Long getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(Long releaseId) {
        this.releaseId = releaseId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }


}
