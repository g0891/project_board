package com.example.board.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "person")
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
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

    public PersonEntity(long id, String name) {
        this.id = id;
        this.name = name;
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
}
