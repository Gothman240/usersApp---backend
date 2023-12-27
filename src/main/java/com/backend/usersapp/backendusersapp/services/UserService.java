package com.backend.usersapp.backendusersapp.services;

import com.backend.usersapp.backendusersapp.models.dto.UserDto;
import com.backend.usersapp.backendusersapp.models.entities.User;
import com.backend.usersapp.backendusersapp.models.request.UserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> findAll();
    Optional<UserDto> findById(Long id);
    UserDto save(User user);
    Optional<UserDto> update( UserRequest user, Long id);
    void remove(Long id);
}
