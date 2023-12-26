package com.backend.usersapp.backendusersapp.auth.filters;

import static com.backend.usersapp.backendusersapp.auth.TokenJwtConfig.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;

/* se ejecuta en cada request*/
public class JwtValidationFilter extends BasicAuthenticationFilter {
    public JwtValidationFilter( AuthenticationManager authenticationManager ) {
        super( authenticationManager );
    }

    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain chain ) throws IOException, ServletException {
        String header = request.getHeader( HEADER_AUTHORIZATION );

        //se valida que sea distinto de nulo y si no comienza con "Bearer"
        if ( header == null || !header.startsWith( PREFIX_TOKEN ) ) {
            chain.doFilter( request, response );
            return;
        }

        //obtener token desde la cabecera

        String token = header.replace( PREFIX_TOKEN, "" );

        try {
            Claims claims = Jwts.parser().verifyWith( (SecretKey) SECRET_KEY ).build().parseSignedClaims( token ).getPayload();

            String username = claims.getSubject();

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add( new SimpleGrantedAuthority( "ROLE_USER" ) );

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( username, null, authorities );
            SecurityContextHolder.getContext().setAuthentication( authenticationToken );
            chain.doFilter( request, response );
        }catch (JwtException e){
            Map<String, String> body = new HashMap<>();
            body.put( "error", e.getMessage() );
            body.put( "message", "el token no es valido" );

            response.getWriter().write( new ObjectMapper().writeValueAsString( body ) );
            response.setStatus( 403 );
            response.setContentType( "application/json" );
        }
    }
}
