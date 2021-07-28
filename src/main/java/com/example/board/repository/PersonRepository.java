package com.example.board.repository;

import com.example.board.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
/*    Optional<PersonEntity> findById(Long id);
    List<PersonEntity> findAll();
    PersonEntity save(PersonEntity person);
    void deleteById(Long id);*/
}