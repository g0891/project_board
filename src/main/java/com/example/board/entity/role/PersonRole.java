package com.example.board.entity.role;

import com.example.board.config.security.Permission;
import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public enum PersonRole {
    CUSTOMER(Set.of(
            Permission.PROJECTS_READ,
            Permission.PROJECTS_WRITE,
            Permission.RELEASES_READ,
            Permission.TASKS_READ,
            Permission.BANKING_MANAGEMENT
    )),
    AUTHOR(Set.of(
            Permission.PROJECTS_READ,
            Permission.RELEASES_WRITE,
            Permission.RELEASES_READ,
            Permission.TASKS_READ,
            Permission.TASKS_WRITE,
            Permission.BANKING_VERIFY_PAYMENT
    )),
    EXECUTOR(Set.of(
            Permission.PROJECTS_READ,
            Permission.RELEASES_READ,
            Permission.TASKS_READ,
            Permission.TASKS_WRITE
    )),
    ADMIN(
            Arrays.stream(Permission.values()).collect(Collectors.toSet())
    );

    private final Set<Permission> permissions;

    PersonRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }

    @JsonCreator
    public static PersonRole create(String providedRole) throws BoardAppIncorrectEnumException {
        if (providedRole == null) {
            throw new BoardAppIncorrectEnumException("NULL", PersonRole.class);
        }

        Optional<PersonRole> personRole = Arrays.stream(PersonRole.values())
                .filter( role -> role.name().equalsIgnoreCase(providedRole))
                .findFirst();

        return personRole.orElseThrow(
                () -> new BoardAppIncorrectEnumException(providedRole, PersonRole.class)
        );

    }
}
