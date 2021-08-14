package com.example.board.entity.release;

import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Optional;

public enum ReleaseStatus {
        OPEN,
        CLOSED;

        @JsonCreator
        public static ReleaseStatus create(String providedStatus) throws BoardAppIncorrectEnumException {
                if (providedStatus == null) {
                        throw new BoardAppIncorrectEnumException("NULL", ReleaseStatus.class);
                }

                Optional<ReleaseStatus> releaseStatus = Arrays.stream(ReleaseStatus.values())
                        .filter(status -> status.name().equalsIgnoreCase(providedStatus))
                        .findAny();

                return releaseStatus.orElseThrow(
                        () -> new BoardAppIncorrectEnumException(providedStatus, ReleaseStatus.class)
                );
        }
}
