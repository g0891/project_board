package com.example.board.service;

import com.example.board.rest.dto.release.ReleaseCreateDto;
import com.example.board.rest.dto.release.ReleaseReadDto;
import com.example.board.rest.dto.release.ReleaseUpdateDto;

import java.util.List;

/**
 * Interface for working with releases.
 */
public interface ReleaseService {
    /**
     * Searches and returns information about the release (if found).
     * @param id An id of release to search for
     * @return A release description
     */
    ReleaseReadDto getById(long id);

    /**
     * Retrieving a complete list of releases.
     * @return A list of existing releases
     */
    List<ReleaseReadDto> getAll();

    /**
     * Used to add a release
     * @param release A DTO with parameters required to create a new release
     * @return An id of release created
     */
    long add(ReleaseCreateDto release);

    /*void update(long id,
                Optional<String> newVersion,
                Optional<ReleaseStatus> newStatus);*/

    /**
     * Used to update release information.
     * @param id An id of release to search for
     * @param releaseUpdateDto A DTO with parameters required to update a release
     */
    void update(long id, ReleaseUpdateDto releaseUpdateDto);

    /**
     * Deletes a release from the system.
     * @param id An id of release to search for
     */
    void delete(long id);

    /**
     * Counts uncompleted task in a closed release.
     * @param id An id of release to search for
     * @return A number of uncompleted tasks
     */
    long countCancelledForClosedRelease(long id);
}
