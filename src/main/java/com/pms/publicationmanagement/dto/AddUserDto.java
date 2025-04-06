package com.pms.publicationmanagement.dto;

import com.pms.publicationmanagement.model.user.UserRole;

import java.util.UUID;

public class AddUserDto {

    public  String firstName;

    public String middleName;

    public String lastName;

    public String email;

    public String password;

    public UUID id;

    public UserRole userRole;

    public AddUserDto(String firstName, String middleName, String lastName, String email, String password,
                      UUID id, UserRole userRole) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.id = id;
        this.userRole = userRole;
    }

    public AddUserDto() {
    }
}
