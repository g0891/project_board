package com.example.board.service;

import com.example.board.rest.dto.release.ReleaseCreateDto;
import com.example.board.rest.dto.release.ReleaseReadDto;
import com.example.board.rest.dto.release.ReleaseUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReleaseService {
    ReleaseReadDto getById(long id) throws BoardAppIncorrectIdException;
    List<ReleaseReadDto> getAll();
    long add(ReleaseCreateDto project) throws BoardAppIncorrectIdException;
    void update(long id, ReleaseUpdateDto project) throws BoardAppIncorrectIdException;
    void delete(long id) throws BoardAppIncorrectIdException;
}
