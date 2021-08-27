package com.example.board.service.implementation;

import com.example.board.entity.project.ProjectStatus;
import com.example.board.entity.release.ReleaseEntity;
import com.example.board.entity.release.ReleaseStatus;
import com.example.board.entity.task.TaskStatus;
import com.example.board.mapper.ReleaseMapper;
import com.example.board.repository.ProjectRepository;
import com.example.board.repository.ReleaseRepository;
import com.example.board.rest.dto.release.ReleaseCreateDto;
import com.example.board.rest.dto.release.ReleaseReadDto;
import com.example.board.rest.dto.release.ReleaseUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIllegalArgumentException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectStateException;
import com.example.board.service.ReleaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    public ReleaseReadDto getById(long id) {
        ReleaseEntity releaseEntity = releaseRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException("BoardAppIncorrectIdException.noReleaseFound", id)
        );
        return releaseMapper.releaseEntityToReleaseReadDto(releaseEntity);
    }

    @Override
    public List<ReleaseReadDto> getAll() {
        List<ReleaseEntity> releaseEntities = releaseRepository.findAll();
        return releaseMapper.releaseEntityListToReleaseReadDtoList(releaseEntities);
    }

    @Override
    public long add(ReleaseCreateDto release) {
        ReleaseEntity releaseEntity = releaseMapper.releaseCreateDtoToReleaseEntity(release);

        if (releaseEntity.getProject().getStatus() != ProjectStatus.STARTED) {
            throw new BoardAppIncorrectStateException("BoardAppIncorrectStateException.projectNotStarted");
        }

        if (StringUtils.isBlank(releaseEntity.getVersion())) {
            throw new BoardAppIllegalArgumentException("BoardAppIllegalArgumentException.releaseVersionIsEmpty");
        }

        releaseEntity = releaseRepository.save(releaseEntity);
        return releaseEntity.getId();
    }

    /*@Override
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
    }*/

    @Override
    public void update(long id, ReleaseUpdateDto releaseUpdateDto) {
        ReleaseEntity releaseEntity = releaseRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException("BoardAppIncorrectIdException.noReleaseFound", id)
        );

        if (releaseEntity.getStatus() == ReleaseStatus.CLOSED) {
            throw new BoardAppIncorrectStateException("BoardAppIncorrectStateException.releaseClosed");
        }

        ReleaseStatus newReleaseStatus = releaseUpdateDto.getStatus();
        if (newReleaseStatus == ReleaseStatus.CLOSED) {
            releaseEntity.getTasks().stream()
                    .forEach(task -> {if (task.getStatus() != TaskStatus.DONE && task.getStatus() != TaskStatus.CANCELED) {
                        task.setStatus(TaskStatus.CANCELED);
                        task.setDoneOn(LocalDateTime.now());
                    }});
            releaseEntity.setReleasedOn(LocalDateTime.now());
            releaseEntity.setStatus(ReleaseStatus.CLOSED);
        }

        String newVersion = releaseUpdateDto.getVersion();
        if (newVersion != null) {
            if (StringUtils.isBlank(newVersion)) {
                throw new BoardAppIllegalArgumentException("BoardAppIllegalArgumentException.releaseVersionIsEmpty");
            }
            releaseEntity.setVersion(newVersion);
        }

        releaseRepository.save(releaseEntity);
    }

    @Override
    public void delete(long id) {
        ReleaseEntity release = releaseRepository.findById(id).orElseThrow(
                () ->new BoardAppIncorrectIdException("BoardAppIncorrectIdException.noReleaseFound", id)
        );

        if (!release.getTasks().isEmpty()) {
            throw new BoardAppIncorrectStateException("BoardAppIncorrectStateException.deleteWithTasks");
        }

        releaseRepository.deleteById(id);
    }

    @Override
    public long countCancelledForClosedRelease(long id) {
        ReleaseEntity releaseEntity = releaseRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException("BoardAppIncorrectIdException.noReleaseFound", id)
        );

        if (releaseEntity.getStatus() != ReleaseStatus.CLOSED) {
            throw new BoardAppIncorrectStateException("BoardAppIncorrectStateException.releaseNotClosed");
        }

        return releaseEntity.getTasks().stream().filter(task -> task.getStatus() == TaskStatus.CANCELED).count();
    }
}
