package com.pms.publicationmanagement.dto;


import com.pms.publicationmanagement.model.UserType;

public class RegisterRequestDto {

    public String firstName;
    public String middleName;
    public String lastName;
    public String email;
    public String password;
    public Integer id;
    public UserType userType;
    public String institutionName;

    public String address;
    public String phoneNumber;
    public String institutionEmail;

    public RegisterRequestDto(String firstName, String middleName, String lastName, String email, String password, Integer id, UserType userType,
                              String name, String address, String phoneNumber, String institutionEmail) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.id = id;
        this.userType = userType;
        this.institutionName = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.institutionEmail = institutionEmail;
    }

    public RegisterRequestDto() {
    }
}
