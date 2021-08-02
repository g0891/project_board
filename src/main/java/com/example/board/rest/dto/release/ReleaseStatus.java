package com.example.board.rest.dto.release;

import com.example.board.rest.dto.project.ProjectStatus;
import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum ReleaseStatus {
        OPEN,
        CLOSED;

        @JsonCreator
        public static ReleaseStatus create(String providedStatus) throws BoardAppIncorrectEnumException {
                if (providedStatus == null) {
                        throw new BoardAppIncorrectEnumException("NULL", ReleaseStatus.class);
                }
                for (ReleaseStatus s: ReleaseStatus.values()) {
                        if (s.name().equalsIgnoreCase(providedStatus)) {
                                return s;
                        }
                }
                throw new BoardAppIncorrectEnumException(providedStatus, ReleaseStatus.class);
        }
}
