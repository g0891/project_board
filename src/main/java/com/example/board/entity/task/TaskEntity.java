package com.example.board.entity.task;

import com.example.board.entity.person.PersonEntity;
import com.example.board.entity.release.ReleaseEntity;

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
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "task")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "done_on")
    private LocalDateTime doneOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private PersonEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_id")
    private PersonEntity executor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "release_id")
    private ReleaseEntity release;

    public static class Builder{
        private Long id;
        private String name;
        private String description;
        private TaskStatus status;
        private LocalDateTime createdOn;
        private LocalDateTime doneOn;
        private PersonEntity author;
        private PersonEntity executor;
        private ReleaseEntity release;

        public Builder addId(Long id) {
            this.id = id;
            return this;
        }
        public Builder addName(String name) {
            this.name = name;
            return this;
        }
        public Builder addDescription(String description) {
            this.description = description;
            return this;
        }
        public Builder addStatus(TaskStatus status) {
            this.status = status;
            return this;
        }
        public Builder addCreatedOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }
        public Builder addDoneOn(LocalDateTime doneOn) {
            this.doneOn = doneOn;
            return this;
        }
        public Builder addAuthor(PersonEntity author) {
            this.author = author;
            return this;
        }
        public Builder addExecutor(PersonEntity executor) {
            this.executor = executor;
            return this;
        }
        public Builder addRelease(ReleaseEntity release) {
            this.release = release;
            return this;
        }

        public TaskEntity build(){
            return new TaskEntity(id,name,description,status,createdOn,doneOn,author,executor,release);
        }
    }

    public TaskEntity() {
    }

    public TaskEntity(Long id, String name, String description, TaskStatus status, LocalDateTime createdOn, LocalDateTime doneOn, PersonEntity author, PersonEntity executor, ReleaseEntity release) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.createdOn = createdOn;
        this.doneOn = doneOn;
        this.author = author;
        this.executor = executor;
        this.release = release;
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

    public PersonEntity getAuthor() {
        return author;
    }

    public void setAuthor(PersonEntity author) {
        this.author = author;
    }

    public PersonEntity getExecutor() {
        return executor;
    }

    public void setExecutor(PersonEntity executor) {
        this.executor = executor;
    }

    public ReleaseEntity getRelease() {
        return release;
    }

    public void setRelease(ReleaseEntity release) {
        this.release = release;
    }
}
