package com.example.board.service.specification;

import com.example.board.entity.task.TaskEntity;
import com.example.board.entity.task.TaskStatus;
import com.example.board.rest.dto.task.TaskSearchDto;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


public class TaskSpecification {
   /*public static Specification<TaskEntity> get(Long id,
                                  String name,
                                  String description,
                                  TaskStatus status,
                                  Long authorId,
                                  Long executorId,
                                  Long releaseId,
                                  Long projectId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (id != null) { predicates.add(criteriaBuilder.equal(root.get("id"), id)); }
            if (name != null && !name.isEmpty()) { predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%")); }
            if (description != null && !description.isEmpty()) { predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%")); }
            if (status != null) { predicates.add(criteriaBuilder.equal(root.get("status"), status)); }
            if (authorId != null) { predicates.add(criteriaBuilder.equal(root.get("author").get("id"), authorId)); }
            if (executorId != null) { predicates.add(criteriaBuilder.equal(root.get("executor").get("id"), executorId)); }
            if (releaseId != null) { predicates.add(criteriaBuilder.equal(root.get("release").get("id"), releaseId)); }
            if (projectId != null) { predicates.add(criteriaBuilder.equal(root.get("release").get("project").get("id"), projectId)); }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }*/

    public static Specification<TaskEntity> get(TaskSearchDto taskSearchDto) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Long id = taskSearchDto.getId();
            String name = taskSearchDto.getName();
            String description = taskSearchDto.getDescription();
            TaskStatus status = taskSearchDto.getStatus();
            Long authorId = taskSearchDto.getAuthorId();
            Long executorId = taskSearchDto.getExecutorId();
            Long releaseId = taskSearchDto.getReleaseId();
            Long projectId = taskSearchDto.getProjectId();

            if (id != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), id));
            }
            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (description != null && !description.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (authorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("author").get("id"), authorId));
            }
            if (executorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("executor").get("id"), executorId));
            }
            if (releaseId != null) {
                predicates.add(criteriaBuilder.equal(root.get("release").get("id"), releaseId));
            }
            if (projectId != null) {
                predicates.add(criteriaBuilder.equal(root.get("release").get("project").get("id"), projectId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
