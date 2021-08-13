package com.example.board.entity.person;

import com.example.board.entity.project.ProjectEntity;
import com.example.board.entity.role.RoleEntity;
import com.example.board.entity.task.TaskEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;


@Entity
@Table(name = "person")
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String password;
    @Enumerated(EnumType.STRING)
    private PersonStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "person_role",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private Set<ProjectEntity> projectsWhereCustomer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private Set<TaskEntity> tasksWhereAuthor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "executor")
    private Set<TaskEntity> tasksWhereExecutor;

    public PersonEntity() {
    }

    public PersonEntity(String name, String password, PersonStatus status) {
        this.name = name;
        this.password = password;
        this.status = status;
    }

    public PersonEntity(String name, Set<RoleEntity> roles) {
        this.name = name;
        this.roles = roles;
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

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public Set<ProjectEntity> getProjectsWhereCustomer() {
        return projectsWhereCustomer;
    }

    public void setProjectsWhereCustomer(Set<ProjectEntity> projectsWhereCustomer) {
        this.projectsWhereCustomer = projectsWhereCustomer;
    }

    public Set<TaskEntity> getTasksWhereAuthor() {
        return tasksWhereAuthor;
    }

    public void setTasksWhereAuthor(Set<TaskEntity> tasksWhereAuthor) {
        this.tasksWhereAuthor = tasksWhereAuthor;
    }

    public Set<TaskEntity> getTasksWhereExecutor() {
        return tasksWhereExecutor;
    }

    public void setTasksWhereExecutor(Set<TaskEntity> tasksWhereExecutor) {
        this.tasksWhereExecutor = tasksWhereExecutor;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PersonStatus getStatus() {
        return status;
    }

    public void setStatus(PersonStatus status) {
        this.status = status;
    }
}
