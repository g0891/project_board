package com.example.board.entity.role;

import com.example.board.config.security.Permission;
import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonRoleTest {

    @Test
    void getAuthorities() {
        assertEquals(PersonRole.CUSTOMER.getPermissions().size(), PersonRole.CUSTOMER.getAuthorities().size());
        assertTrue(PersonRole.AUTHOR.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(Permission.RELEASES_WRITE.getPermission())));
    }

    @Test
    void create() {
        assertThrows(BoardAppIncorrectEnumException.class, () -> PersonRole.create(null));
        assertThrows(BoardAppIncorrectEnumException.class, () -> PersonRole.create("notExistingValue"));
        for (PersonRole value: PersonRole.values()) {
            assertEquals(value, PersonRole.create(value.name()));
        }
    }
}