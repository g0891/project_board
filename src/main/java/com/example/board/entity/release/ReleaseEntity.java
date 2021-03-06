package com.example.board.entity.release;

import com.example.board.entity.project.ProjectEntity;
import com.example.board.entity.task.TaskEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "release")
public class ReleaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String version;
    @Enumerated(EnumType.STRING)
    private ReleaseStatus status;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "released_on")
    private LocalDateTime releasedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "release")
    private List<TaskEntity> tasks;

    public static class Builder{
        private Long id;
        private String version;
        private ReleaseStatus status;
        private LocalDateTime createdOn;
        private LocalDateTime releasedOn;
        private ProjectEntity project;
        private List<TaskEntity> tasks;

        public Builder addId(Long id) {
            this.id = id;
            return this;
        }
        public Builder addVersion(String version) {
            this.version = version;
            return this;
        }
        public Builder addStatus(ReleaseStatus status) {
            this.status = status;
            return this;
        }
        public Builder addCreatedOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Builder addReleasedOn(LocalDateTime releasedOn) {
            this.releasedOn = releasedOn;
            return this;
        }

        public Builder addProject(ProjectEntity project) {
            this.project = project;
            return this;
        }
        public Builder addTasks(List<TaskEntity> tasks) {
            this.tasks = tasks;
            return this;
        }
        public ReleaseEntity build(){
            ReleaseEntity releaseEntity = new ReleaseEntity(version,status,createdOn,releasedOn,project);
            releaseEntity.setId(id);
            releaseEntity.setTasks(tasks);
            return releaseEntity;
        }
    }

    public ReleaseEntity() {
    }

    public ReleaseEntity(String version, ReleaseStatus status, LocalDateTime createdOn, LocalDateTime releasedOn, ProjectEntity project) {
        this.version = version;
        this.status = status;
        this.createdOn = createdOn;
        this.releasedOn = releasedOn;
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getReleasedOn() {
        return releasedOn;
    }

    public void setReleasedOn(LocalDateTime releasedOn) {
        this.releasedOn = releasedOn;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }
}
