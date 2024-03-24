package com.pms.publicationmanagement.dto;

import com.pms.publicationmanagement.model.UserType;

public class AddUserDto {

    public  String firstName;

    public String middleName;

    public String lastName;

    public String email;

    public String password;

    public Integer id;

    public UserType userType;

    public AddUserDto(String firstName, String middleName, String lastName, String email, String password,
                      Integer id, UserType userType) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.id = id;
        this.userType = userType;
    }

    public AddUserDto() {
    }
}
