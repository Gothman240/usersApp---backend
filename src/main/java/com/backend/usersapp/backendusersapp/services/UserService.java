package com.backend.usersapp.backendusersapp.services;

import com.backend.usersapp.backendusersapp.models.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    Optional<User> update(User user, Long id);
    void remove(Long id);
}
