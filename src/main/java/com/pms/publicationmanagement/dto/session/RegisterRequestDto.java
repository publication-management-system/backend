package com.pms.publicationmanagement.dto.session;


import jakarta.validation.constraints.NotEmpty;

public class RegisterRequestDto {
    @NotEmpty(message = "must not be empty")
    public String email;
    @NotEmpty(message = "must not be empty")
    public String firstName;
    @NotEmpty(message = "must not be empty")
    public String lastName;
    @NotEmpty(message = "must not be empty")
    public String middleName;
    @NotEmpty(message = "must not be empty")
    public String password;
    @NotEmpty(message = "must not be empty")
    public String institutionAddress;
    @NotEmpty(message = "must not be empty")
    public String institutionName;
    public String institutionPhoneNumber;
    @NotEmpty(message = "must not be empty")
    public String institutionEmailAddress;
}
