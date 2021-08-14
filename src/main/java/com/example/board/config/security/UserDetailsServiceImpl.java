package com.example.board.config.security;

import com.example.board.entity.person.PersonEntity;
import com.example.board.entity.person.PersonStatus;
import com.example.board.repository.PersonRepository;
import com.example.board.rest.errorController.exception.BoardAppIncorrectStateException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service/*("myUserDetailsService")*/
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PersonRepository personRepository;

    public UserDetailsServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PersonEntity personEntity = personRepository.findByName(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User '%s' not found", username))
        );
        if (personEntity.getStatus() != PersonStatus.ACTIVE) {
            throw new BoardAppIncorrectStateException("User is not ACTIVE thus can't log in");
        }
        return SecurityUser.fromPersonEntity(personEntity);
    }
}
