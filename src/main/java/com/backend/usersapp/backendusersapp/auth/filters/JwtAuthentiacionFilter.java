package com.backend.usersapp.backendusersapp.auth.filters;

import com.backend.usersapp.backendusersapp.models.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/* maneja ruta /login, solo se ejecuta cuando sea POST -> attempt*/
public class JwtAuthentiacionFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JwtAuthentiacionFilter( AuthenticationManager authenticationManager ) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response ) throws AuthenticationException {
        User user = null;
        String password = null;
        String username = null;

        try {
            user = new ObjectMapper().readValue( request.getInputStream(), User.class );
            username = user.getUsername();
            password = user.getPassword();
        } catch (IOException e) {
            throw new RuntimeException( e );
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken( username, password );
        return authenticationManager.authenticate( token );
    }

    @Override
    protected void successfulAuthentication( HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult ) throws IOException, ServletException {
        String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();

        String originalInput = "algun_token." + username;
        String token = Base64.getEncoder().encodeToString( originalInput.getBytes() );

        response.addHeader( "Authorization", "Bearer " + token );
        Map<String, Object> body = new HashMap<>();
        body.put( "token", token );
        body.put( "message", String.format( "Hola %s, has iniciado sesion con exito!", username ) );
        body.put( "username", username );
        response.getWriter().write( new ObjectMapper().writeValueAsString( body ) );
        response.setStatus( 200 );
        response.setContentType( "application/json" );
    }

    @Override
    protected void unsuccessfulAuthentication( HttpServletRequest request, HttpServletResponse response, AuthenticationException failed ) throws IOException, ServletException {
        Map<String, Object> body = new HashMap<>();
        body.put( "message", "Error en la autenticación username o password incorrecto" );
        body.put( "error", failed.getMessage() );

        response.getWriter().write( new ObjectMapper().writeValueAsString( body ) );
        response.setStatus( 401 );
        response.setContentType( "application/json" );

    }
}
