package com.backend.usersapp.backendusersapp.auth;

import com.backend.usersapp.backendusersapp.auth.filters.JwtAuthentiacionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain( HttpSecurity httpSecurity ) throws Exception {
        httpSecurity.authorizeHttpRequests( req -> req.requestMatchers( HttpMethod.GET, "/users" ).permitAll()
                        .anyRequest().authenticated()
                ).addFilter( new JwtAuthentiacionFilter( authenticationConfiguration.getAuthenticationManager() ) )
                .csrf( config -> config.disable() )
                .sessionManagement( management -> management.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) );
        return httpSecurity.build();
    }
}
