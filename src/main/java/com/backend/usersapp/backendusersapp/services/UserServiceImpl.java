package com.backend.usersapp.backendusersapp.services;

import com.backend.usersapp.backendusersapp.models.dto.UserDto;
import com.backend.usersapp.backendusersapp.models.dto.mapper.DtoMapperUser;
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
import java.util.stream.Collectors;

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
    public List<UserDto> findAll() {
        List<User> userList = (List<User>) userRepository.findAll();

        return userList
                .stream()
                .map( user -> DtoMapperUser.builder().setUser( user ).build() )
                .collect( Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById( Long id) {
        return userRepository.findById(id).map( user -> DtoMapperUser.builder().setUser( user ).build() );

    }

    @Override
    @Transactional
    public UserDto save(User user) {
        user.setPassword( passwordEncoder.encode(user.getPassword()) );

        Optional<Role> optionalRole = roleRepository.findByName( "ROLE_USER" );

        List<Role> roleList = new ArrayList<>();

        if ( optionalRole.isPresent() ) {
            roleList.add( optionalRole.orElseThrow() );
        }
        user.setRoles( roleList );
        return DtoMapperUser.builder().setUser( userRepository.save(user) ).build();
    }

    @Override
    @Transactional
    public Optional<UserDto> update( UserRequest user, Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User userOptional = null;
        if (optionalUser.isPresent()) {
            User userDb = optionalUser.orElseThrow();
            userDb.setUsername(user.getUsername());
            userDb.setEmail(user.getEmail());
            userOptional = userRepository.save( userDb );
        }
        return Optional.ofNullable( DtoMapperUser.builder().setUser( userOptional ).build() );
    }

    @Override
    @Transactional
    public void remove(Long id) {
        userRepository.deleteById(id);
    }
}
