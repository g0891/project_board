package com.example.board.entity;

import com.example.board.rest.dto.release.ReleaseStatus;

import javax.persistence.*;
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
