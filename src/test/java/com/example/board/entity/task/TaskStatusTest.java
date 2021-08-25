package com.example.board.entity.task;

import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskStatusTest {

    @Test
    void create() {
        assertThrows(BoardAppIncorrectEnumException.class, () -> TaskStatus.create(null));
        assertThrows(BoardAppIncorrectEnumException.class, () -> TaskStatus.create("notExistingValue"));
        for (TaskStatus value: TaskStatus.values()) {
            assertEquals(value, TaskStatus.create(value.name()));
        }
    }
}