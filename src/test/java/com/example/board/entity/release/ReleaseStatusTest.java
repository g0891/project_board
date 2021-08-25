package com.example.board.entity.release;

import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReleaseStatusTest {

    @Test
    void create() {
        assertThrows(BoardAppIncorrectEnumException.class, () -> ReleaseStatus.create(null));
        assertThrows(BoardAppIncorrectEnumException.class, () -> ReleaseStatus.create("notExistingValue"));
        for (ReleaseStatus value: ReleaseStatus.values()) {
            assertEquals(value, ReleaseStatus.create(value.name()));
        }
//        assertEquals(ReleaseStatus.OPEN, ReleaseStatus.create("open"));
//        assertEquals(ReleaseStatus.CLOSED, ReleaseStatus.create("closed"));
    }
}