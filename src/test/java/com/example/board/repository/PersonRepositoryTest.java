/*
package com.example.board.repository;

import com.example.board.entity.person.PersonEntity;
import com.example.board.entity.person.PersonStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("integration")
class PersonRepositoryTest {

    private final PersonRepository personRepository;

    @Autowired
    public PersonRepositoryTest( PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Test
    void findByName() {
        String name = "Antony";
        String someOtherName = "Jane";
        PersonEntity person = new PersonEntity(name, "password", PersonStatus.ACTIVE);
        personRepository.save(person);

        Optional<PersonEntity> personFromRepository;

        personFromRepository = personRepository.findByName(name);
        assertThat(personFromRepository.isPresent()).isTrue();

        personFromRepository = personRepository.findByName(someOtherName);
        assertThat(personFromRepository.isPresent()).isFalse();
    }

}
*/
