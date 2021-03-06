package com.example.board.entity.task;

import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Optional;

public enum TaskStatus {
    BACKLOG,
    IN_PROGRESS,
    DONE,
    CANCELED;

    @JsonCreator
    public static TaskStatus create(String providedStatus) throws BoardAppIncorrectEnumException {
        if (providedStatus == null) {
            throw new BoardAppIncorrectEnumException("NULL", TaskStatus.class);
        }

        Optional<TaskStatus> taskStatus = Arrays.stream(TaskStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(providedStatus))
                .findFirst();

        return taskStatus.orElseThrow(
                () -> new BoardAppIncorrectEnumException(providedStatus, TaskStatus.class)
        );
    }
}
