package com.backend.usersapp.backendusersapp.auth.filters;

import static com.backend.usersapp.backendusersapp.auth.TokenJwtConfig.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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

        if ( header == null || !header.startsWith( PREFIX_TOKEN ) ) {
            chain.doFilter( request, response );
            return;
        }

        String token = header.replace( PREFIX_TOKEN, "" );
        byte [] tokenDecodeBytes = Base64.getDecoder().decode( token );
        String tokenDecode = new String(tokenDecodeBytes);

        String [] tokenArr = tokenDecode.split( "\\." );
        String secret = tokenArr[0];
        String username = tokenArr[1];

        if ( SECRET_KEY.equals( secret ) ) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add( new SimpleGrantedAuthority( "ROLE_USER" ) );

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( username, null, authorities );
            SecurityContextHolder.getContext().setAuthentication( authenticationToken );
            chain.doFilter( request, response );
        }else {
            Map<String, String> body = new HashMap<>();
            body.put( "message", "el token no es valido" );

            response.getWriter().write( new ObjectMapper().writeValueAsString( body ) );
            response.setStatus( 403 );
            response.setContentType( "application/json" );
        }
    }
}
