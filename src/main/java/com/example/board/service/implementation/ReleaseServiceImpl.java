package com.example.board.service.implementation;

import com.example.board.entity.ProjectEntity;
import com.example.board.entity.ReleaseEntity;
import com.example.board.mapper.ReleaseMapper;
import com.example.board.repository.ProjectRepository;
import com.example.board.repository.ReleaseRepository;
import com.example.board.rest.dto.release.ReleaseCreateDto;
import com.example.board.rest.dto.release.ReleaseReadDto;
import com.example.board.rest.dto.release.ReleaseStatus;
import com.example.board.rest.dto.release.ReleaseUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.service.ReleaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class ReleaseServiceImpl implements ReleaseService {

    @Autowired
    ReleaseRepository releaseRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ReleaseMapper releaseMapper;

    @Override
    public ReleaseReadDto getById(long id) throws BoardAppIncorrectIdException {
        ReleaseEntity releaseEntity = releaseRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no release with id = %d", id))
        );
        return releaseMapper.releaseEntityToReleaseReadDto(releaseEntity);
    }

    @Override
    public List<ReleaseReadDto> getAll() {
        List<ReleaseEntity> releaseEntities = releaseRepository.findAll();
        return releaseMapper.releaseEntityListToReleaseReadDtoList(releaseEntities);
    }

    @Override
    public long add(ReleaseCreateDto release) throws BoardAppIncorrectIdException {
        ReleaseEntity releaseEntity = releaseMapper.releaseCreateDtoToReleaseEntity(release);
        releaseEntity = releaseRepository.save(releaseEntity);
        return releaseEntity.getId();
    }

    @Override
    public void update(long id, ReleaseUpdateDto release) throws BoardAppIncorrectIdException {
        ReleaseEntity releaseEntity = releaseRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no release with id = %d", id))
        );
        ProjectEntity projectEntity = projectRepository.findById(release.getProjectId()).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no project with id = %d", release.getProjectId()))
        );

        releaseEntity.setVersion(release.getVersion());
        releaseEntity.setProject(projectEntity);
        if (releaseEntity.getStatus() == ReleaseStatus.OPEN && release.getStatus() == ReleaseStatus.CLOSED) {
            releaseEntity.setReleasedOn(LocalDateTime.now());
        }
        releaseEntity.setStatus(release.getStatus());
        releaseRepository.save(releaseEntity);

    }

    @Override
    public void delete(long id) throws BoardAppIncorrectIdException {
        if (releaseRepository.existsById(id)) {
            releaseRepository.deleteById(id);
        } else {
            throw new BoardAppIncorrectIdException(String.format("There is no release with id = %d", id));
        }
    }
}
