package com.example.board.service;

import com.example.board.rest.dto.release.ReleaseCreateDto;
import com.example.board.rest.dto.release.ReleaseReadDto;
import com.example.board.entity.release.ReleaseStatus;
import com.example.board.rest.dto.release.ReleaseUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;

import java.util.List;
import java.util.Optional;

public interface ReleaseService {
    ReleaseReadDto getById(long id) throws BoardAppIncorrectIdException;
    List<ReleaseReadDto> getAll();
    long add(ReleaseCreateDto project) throws BoardAppIncorrectIdException;
    /*void update(long id,
                Optional<String> newVersion,
                Optional<ReleaseStatus> newStatus);*/
    void update(long id, ReleaseUpdateDto releaseUpdateDto);
    void delete(long id) throws BoardAppIncorrectIdException;
    long countCancelledForClosedRelease(long id);
}
