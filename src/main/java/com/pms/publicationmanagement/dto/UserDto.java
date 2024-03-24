package com.pms.publicationmanagement.dto;


import com.pms.publicationmanagement.model.UserType;

public class UserDto {

    public String firstName;

    public String middleName;

    public String lastName;

    //public String email;

    public Integer id;

    public UserType userType;

    public UserDto() {
    }

    public UserDto(String firstName, String middleName, String lastName, Integer id, UserType userType) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.id = id;
        this.userType = userType;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s", firstName, middleName, lastName, id, userType);
    }
}
