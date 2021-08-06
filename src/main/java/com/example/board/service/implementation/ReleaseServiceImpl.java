package com.example.board.service.implementation;

import com.example.board.entity.ProjectEntity;
import com.example.board.entity.ReleaseEntity;
import com.example.board.mapper.ReleaseMapper;
import com.example.board.repository.ProjectRepository;
import com.example.board.repository.ReleaseRepository;
import com.example.board.rest.dto.project.ProjectStatus;
import com.example.board.rest.dto.release.ReleaseCreateDto;
import com.example.board.rest.dto.release.ReleaseReadDto;
import com.example.board.rest.dto.release.ReleaseStatus;
//import com.example.board.rest.dto.release.ReleaseUpdateDto;
import com.example.board.rest.dto.task.TaskStatus;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectStateException;
import com.example.board.service.ReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReleaseServiceImpl implements ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final ProjectRepository projectRepository;
    private final ReleaseMapper releaseMapper;

    //@Autowired
    public ReleaseServiceImpl(ReleaseRepository releaseRepository, ProjectRepository projectRepository, ReleaseMapper releaseMapper) {
        this.releaseRepository = releaseRepository;
        this.projectRepository = projectRepository;
        this.releaseMapper = releaseMapper;
    }


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

        if (releaseEntity.getProject().getStatus() == ProjectStatus.CLOSED) {
            throw new BoardAppIncorrectStateException("Can't add release to the CLOSED project.");
        }

        releaseEntity = releaseRepository.save(releaseEntity);
        return releaseEntity.getId();
    }

    @Override
    public void update(long id, Optional<String> newVersion, Optional<ReleaseStatus> newStatus) {
        ReleaseEntity releaseEntity = releaseRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no release with id = %d", id))
        );

        if (releaseEntity.getStatus() == ReleaseStatus.CLOSED) {
            throw new BoardAppIncorrectStateException("Can't change an already CLOSED release");
        }

        if (newStatus.isPresent() && (newStatus.get() == ReleaseStatus.CLOSED)) {
            releaseEntity.getTasks().stream()
                    .filter(task -> task.getStatus() != TaskStatus.DONE && task.getStatus() != TaskStatus.CANCELED)
                    .forEach(task -> {
                        task.setStatus(TaskStatus.CANCELED);
                        task.setDoneOn(LocalDateTime.now());
                    });
            releaseEntity.setReleasedOn(LocalDateTime.now());
            releaseEntity.setStatus(ReleaseStatus.CLOSED);
        }

        newVersion.ifPresent(releaseEntity::setVersion);

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

    @Override
    public long countCancelledForClosedRelease(long id) {
        ReleaseEntity releaseEntity = releaseRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no release with id = %d", id))
        );

        if (releaseEntity.getStatus() != ReleaseStatus.CLOSED) {
            throw new BoardAppIncorrectStateException("Can't count undone tasks cause release in to CLOSED yet.");
        }

        return releaseEntity.getTasks().stream().filter(task -> task.getStatus() == TaskStatus.CANCELED).count();
    }
}
