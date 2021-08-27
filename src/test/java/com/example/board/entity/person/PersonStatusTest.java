package com.example.board.entity.person;

import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonStatusTest {

    @Test
    void create() {
        assertThrows(BoardAppIncorrectEnumException.class, () -> PersonStatus.create(null));
        assertThrows(BoardAppIncorrectEnumException.class, () -> PersonStatus.create("notExistingValue"));
        for (PersonStatus value: PersonStatus.values()) {
            assertEquals(value, PersonStatus.create(value.name()));
        }
//        assertEquals(PersonStatus.ACTIVE, PersonStatus.create("active"));
//        assertEquals(PersonStatus.DEACTIVATED, PersonStatus.create("deactivated"));
    }
}