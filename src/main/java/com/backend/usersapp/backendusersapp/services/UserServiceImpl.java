package com.backend.usersapp.backendusersapp.services;

import com.backend.usersapp.backendusersapp.models.entities.Role;
import com.backend.usersapp.backendusersapp.models.entities.User;
import com.backend.usersapp.backendusersapp.models.request.UserRequest;
import com.backend.usersapp.backendusersapp.repositories.RoleRepository;
import com.backend.usersapp.backendusersapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setPassword( passwordEncoder.encode(user.getPassword()) );

        Optional<Role> optionalRole = roleRepository.findByName( "ROLE_USER" );

        List<Role> roleList = new ArrayList<>();

        if ( optionalRole.isPresent() ) {
            roleList.add( optionalRole.orElseThrow() );
        }
        user.setRoles( roleList );
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> update( UserRequest user, Long id) {
        Optional<User> optionalUser = this.findById(id);
        if (optionalUser.isPresent()) {
            User userDb = optionalUser.orElseThrow();
            userDb.setUsername(user.getUsername());
            userDb.setEmail(user.getEmail());
            return Optional.of(this.save(userDb));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void remove(Long id) {
        userRepository.deleteById(id);
    }
}
