package com.example.board.rest.dto.task;

import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum TaskStatus {
    BACKLOG,
    IN_PROGRESS,
    DONE;

    @JsonCreator
    public static TaskStatus create(String providedStatus) throws BoardAppIncorrectEnumException {
        if (providedStatus == null) {
            throw new BoardAppIncorrectEnumException("NULL", TaskStatus.class);
        }
        for (TaskStatus s: TaskStatus.values()) {
            if (s.name().equalsIgnoreCase(providedStatus)) {
                return s;
            }
        }
        throw new BoardAppIncorrectEnumException(providedStatus, TaskStatus.class);
    }
}
