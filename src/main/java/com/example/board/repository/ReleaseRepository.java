package com.example.board.repository;

import com.example.board.entity.ReleaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseRepository extends JpaRepository<ReleaseEntity, Long> {
}
