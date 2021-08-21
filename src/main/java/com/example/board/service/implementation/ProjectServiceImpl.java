package com.example.board.service.implementation;

import com.example.board.entity.person.PersonEntity;
import com.example.board.entity.project.ProjectEntity;
import com.example.board.entity.project.ProjectStatus;
import com.example.board.entity.release.ReleaseStatus;
import com.example.board.entity.role.PersonRole;
import com.example.board.feign.BankingService;
import com.example.board.mapper.PersonMapper;
import com.example.board.mapper.ProjectMapper;
import com.example.board.repository.PersonRepository;
import com.example.board.repository.ProjectRepository;
import com.example.board.rest.dto.project.ProjectCreateDto;
import com.example.board.rest.dto.project.ProjectReadDto;
import com.example.board.rest.dto.project.ProjectUpdateDto;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectRoleException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectStateException;
import com.example.board.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final PersonRepository personRepository;
    private final ProjectMapper projectMapper;
    private final PersonMapper personMapper;
    private final BankingService bankingService;

    //@Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, PersonRepository personRepository, ProjectMapper projectMapper, PersonMapper personMapper, BankingService bankingService) {
        this.projectRepository = projectRepository;
        this.personRepository = personRepository;
        this.projectMapper = projectMapper;
        this.personMapper = personMapper;
        this.bankingService = bankingService;
    }

    @Override
    public ProjectReadDto getById(long id) throws BoardAppIncorrectIdException {
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no project with id = %d", id)));
        return projectMapper.projectEntityToProjectReadDto(projectEntity);
    }

    @Override
    public List<ProjectReadDto> getAll() {
        List<ProjectEntity> projectEntities = projectRepository.findAll();
        return projectMapper.projectEntityListToProjectReadDtoList(projectEntities);
    }

    @Override
    public long add(ProjectCreateDto project) throws BoardAppIncorrectIdException {
        ProjectEntity projectEntity = projectMapper.projectCreateDtoToProjectEntity(project);

        if (!personMapper.roleEntitySetToPersonRoleSet(projectEntity.getCustomer().getRoles()).contains(PersonRole.CUSTOMER)) {
            throw new BoardAppIncorrectRoleException("Only person with CUSTOMER role can be a customer of the project.");
        }

        projectEntity = projectRepository.save(projectEntity);
        return projectEntity.getId();
    }

    /*@Override
    public void update(long id,
                       Optional<String> newName,
                       Optional<String> newDescription,
                       Optional<Long> newCustomerId,
                       Optional<ProjectStatus> newStatus) {

        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no project with id = %d", id))
        );

        if (projectEntity.getStatus() == ProjectStatus.CLOSED) {
            throw new BoardAppIncorrectStateException("Can't change an already CLOSED project.");
        }

        if (newStatus.isPresent() && (newStatus.get() == ProjectStatus.CLOSED)) {
            if (projectEntity.getReleases().stream().anyMatch(release -> release.getStatus() != ReleaseStatus.CLOSED)) {
                throw new BoardAppIncorrectStateException("Can't close the project containing a release in a not CLOSED state.");
            }

            projectEntity.setStatus(newStatus.get());
        }

        newName.ifPresent(projectEntity::setName);
        newDescription.ifPresent(projectEntity::setDescription);

        if (newCustomerId.isPresent()) {
            PersonEntity personEntity = personRepository.findById(newCustomerId.get()).orElseThrow(
                    () -> new BoardAppIncorrectIdException(String.format("There is no customer with id = %d", newCustomerId.get()))
            );

            if (!personMapper.roleEntitySetToPersonRoleSet(personEntity.getRoles()).contains(PersonRole.CUSTOMER)) {
                throw new BoardAppIncorrectRoleException("A person can't be set as a customer for a project because the CUSTOMER role is missing.");
            }

            projectEntity.setCustomer(personEntity);
        }

        projectRepository.save(projectEntity);

    }*/

    @Override
    public void update(long id, ProjectUpdateDto projectUpdateDto) {
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no project with id = %d", id))
        );

        if (projectEntity.getStatus() == ProjectStatus.CLOSED) {
            throw new BoardAppIncorrectStateException("Can't change an already CLOSED project.");
        }

        Long newCost = projectUpdateDto.getCost();
        if (newCost != null) {
            if(projectEntity.getStatus() != ProjectStatus.OPEN) {
                throw new BoardAppIncorrectStateException("Can't change cost of a project in a non OPEN state.");
            }
            if (newCost < 0) {
                throw new IllegalArgumentException("The cost could not be less than 0.");
            }

            projectEntity.setCost(newCost);
        }


        ProjectStatus newStatus = projectUpdateDto.getStatus();

        if (newStatus != null && projectEntity.getStatus() != newStatus) {
            switch (newStatus) {
                case CLOSED:
                    if (projectEntity.getReleases().stream().anyMatch(release -> release.getStatus() != ReleaseStatus.CLOSED)) {
                        throw new BoardAppIncorrectStateException("Can't close the project containing a release in a not CLOSED state.");
                    }
                    projectEntity.setStatus(newStatus);
                    break;
                case OPEN:
                    throw new BoardAppIncorrectStateException("Can't change project status back to OPEN.");
                    //break;
                case STARTED:
                    if (projectEntity.getCost() == null) {
                        throw new BoardAppIncorrectStateException("Project cost should be defined first.");
                    }
                    ResponseEntity<Long> response = bankingService.getTotalPaymentsForProject(projectEntity.getId());
                    if (response == null || response.getBody() == null || response.getBody() < projectEntity.getCost()) {
                        throw new BoardAppIncorrectStateException("Customer should pay a proper amount for the project first.");
                    } else {
                        projectEntity.setStatus(ProjectStatus.STARTED);
                    }

            }
        }

/*        if (newStatus == ProjectStatus.CLOSED) {
            if (projectEntity.getReleases().stream().anyMatch(release -> release.getStatus() != ReleaseStatus.CLOSED)) {
                throw new BoardAppIncorrectStateException("Can't close the project containing a release in a not CLOSED state.");
            }
            projectEntity.setStatus(newStatus);
        }*/

        String newName = projectUpdateDto.getName();
        if (newName != null) {
            if (newName.isEmpty()) {
                throw new IllegalArgumentException("Project name can't be an empty string");
            }
            projectEntity.setName(newName);
        }

        String newDescription = projectUpdateDto.getDescription();
        if (newDescription != null) {
            if (newDescription.isEmpty()) {
                throw new IllegalArgumentException("Project description can't be an empty string");
            }
            projectEntity.setDescription(newDescription);
        }

        Long newCustomerId = projectUpdateDto.getCustomerId();
        if (newCustomerId != null) {
            PersonEntity personEntity = personRepository.findById(newCustomerId).orElseThrow(
                    () -> new BoardAppIncorrectIdException(String.format("There is no customer with id = %d", newCustomerId))
            );

            if (!personMapper.roleEntitySetToPersonRoleSet(personEntity.getRoles()).contains(PersonRole.CUSTOMER)) {
                throw new BoardAppIncorrectRoleException("A person can't be set as a customer for a project because the CUSTOMER role is missing.");
            }

            projectEntity.setCustomer(personEntity);
        }

        projectRepository.save(projectEntity);
    }

    @Override
    public void delete(long id) throws BoardAppIncorrectIdException {
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new BoardAppIncorrectIdException(String.format("There is no project with id = %d", id))
        );

        if (!projectEntity.getReleases().isEmpty()) {
            throw new BoardAppIncorrectStateException("Can't delete project with releases inside.");
        }

        projectRepository.deleteById(id);
    }
}
