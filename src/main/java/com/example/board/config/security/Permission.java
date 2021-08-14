package com.example.board.config.security;

public enum Permission {
    PROJECTS_READ("projects:read"),
    PROJECTS_WRITE("projects:write"),
    RELEASES_READ("releases:read"),
    RELEASES_WRITE("releases:write"),
    TASKS_READ("tasks:read"),
    TASKS_WRITE("tasks:write"),
    PERSONS_READ("persons:read"),
    PERSONS_WRITE("persons:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
