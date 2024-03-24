package com.pms.publicationmanagement.mapper;


import com.pms.publicationmanagement.dto.UserDto;
import com.pms.publicationmanagement.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDtoMapper {

    public static UserDto toUserDto(User user){
        UserDto dto = new UserDto();
        dto.firstName = user.getFirstName();
        dto.middleName = user.getMiddleName();
        dto.lastName = user.getLastName();
        dto.id = user.getId();
        dto.userType = user.getUserType();

        return dto;
    }

    public static List<UserDto> toUserDtoList(List <User> userList) {
        List<UserDto> result = new ArrayList<>();

        for(User u : userList){
            result.add(toUserDto(u));
        }
        return result;
    }
}
