package com.backend.usersapp.backendusersapp.models.dto.mapper;

public class DtoMapperUser {
    private static DtoMapperUser mapperUser;
    private DtoMapperUser() {
    }

    public static DtoMapperUser getInstance(){
        mapperUser = new DtoMapperUser();
        return mapperUser;
    }
}
