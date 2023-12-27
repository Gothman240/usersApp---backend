package com.backend.usersapp.backendusersapp.models.dto.mapper;

import com.backend.usersapp.backendusersapp.models.dto.UserDto;
import com.backend.usersapp.backendusersapp.models.entities.User;

public class DtoMapperUser {
    private User user;

    //evitar una instancia de esta clase, la creación de esta clase se maneja mediante el método builder
    private DtoMapperUser() {
    }

    //crea la instancia y devuelve el obj por cada request
    public static DtoMapperUser builder(){
        return new DtoMapperUser();

    }

    public DtoMapperUser setUser(User user){
        this.user = user;
        return this;
    }

    public UserDto build(){
        if ( user == null ) {
            throw new RuntimeException("Debe pasar el entity User");
        }
        return new UserDto(this.user.getId(), this.user.getUsername(), this.user.getEmail());
    }
}
