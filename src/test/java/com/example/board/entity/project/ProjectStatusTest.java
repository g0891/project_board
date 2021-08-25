package com.example.board.entity.project;

import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProjectStatusTest {

    @Test
    void create() {
        assertThrows(BoardAppIncorrectEnumException.class, () -> ProjectStatus.create(null));
        assertThrows(BoardAppIncorrectEnumException.class, () -> ProjectStatus.create("notExistingValue"));
        for (ProjectStatus value: ProjectStatus.values()) {
            assertEquals(value, ProjectStatus.create(value.name()));
        }
    }
}