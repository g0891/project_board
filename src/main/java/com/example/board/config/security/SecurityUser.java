package com.example.board.config.security;

import com.example.board.entity.person.PersonEntity;
import com.example.board.entity.person.PersonStatus;
import com.example.board.entity.role.PersonRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityUser implements UserDetails {
    private final long id;
    private final String username;
    private final String password;
    private final Set<SimpleGrantedAuthority> authorities;
    private final boolean isActive;

    public SecurityUser(long id, String username, String password, Set<SimpleGrantedAuthority> authorities, boolean isActive) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static SecurityUser fromPersonEntity(PersonEntity personEntity) {
        return new SecurityUser(
                personEntity.getId(),
                personEntity.getName(),
                personEntity.getPassword(),
                personEntity.getRoles()
                        .stream()
                        .map(role -> PersonRole.valueOf(role.getName()))
                        .flatMap(personRole -> personRole.getAuthorities().stream())
                        .collect(Collectors.toSet()),
                personEntity.getStatus() == PersonStatus.ACTIVE
        );
    }
}
