package com.backend.usersapp.backendusersapp.repositories;

import com.backend.usersapp.backendusersapp.models.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
