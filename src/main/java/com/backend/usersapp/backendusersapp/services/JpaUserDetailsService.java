package com.backend.usersapp.backendusersapp.services;

import com.backend.usersapp.backendusersapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {

        Optional<com.backend.usersapp.backendusersapp.models.entities.User> optionalUser = userRepository.findByUsername( username );

        if ( optionalUser.isEmpty() ) {
            throw new UsernameNotFoundException( String.format( "Username %s no existe en el sistema!", username ) );
        }

        com.backend.usersapp.backendusersapp.models.entities.User user = optionalUser.orElseThrow();


        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add( new SimpleGrantedAuthority( "ROLE_USER" ) );

        return new User( user.getUsername(),user.getPassword(), true, true, true, true, authorities );

    }
}
