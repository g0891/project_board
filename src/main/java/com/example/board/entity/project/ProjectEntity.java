package com.example.board.entity.project;

import com.example.board.entity.person.PersonEntity;
import com.example.board.entity.release.ReleaseEntity;

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
import java.util.List;

@Entity
@Table(name = "project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private Long cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private PersonEntity customer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private List<ReleaseEntity> releases;

    public static class Builder{
        private Long id;
        private String name;
        private String description;
        private ProjectStatus status;
        private Long cost;
        private PersonEntity customer;
        private List<ReleaseEntity> releases;

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
        public Builder addStatus(ProjectStatus status) {
            this.status = status;
            return this;
        }
        public Builder addCost(Long cost) {
            this.cost = cost;
            return this;
        }
        public Builder addCustomer(PersonEntity customer) {
            this.customer = customer;
            return this;
        }
        public Builder addReleases(List<ReleaseEntity> releases) {
            this.releases = releases;
            return this;
        }
        public ProjectEntity build() {
            ProjectEntity newEntity = new ProjectEntity(name,description,status,cost,customer);
            newEntity.setId(id);
            newEntity.setReleases(releases);
            return newEntity;
        }
    }

    public ProjectEntity() {
    }

    public ProjectEntity(String name, String description, ProjectStatus status, Long cost, PersonEntity customer) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.cost = cost;
        this.customer = customer;
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

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public PersonEntity getCustomer() {
        return customer;
    }

    public void setCustomer(PersonEntity customer) {
        this.customer = customer;
    }

    public List<ReleaseEntity> getReleases() {
        return releases;
    }

    public void setReleases(List<ReleaseEntity> releases) {
        this.releases = releases;
    }
}
