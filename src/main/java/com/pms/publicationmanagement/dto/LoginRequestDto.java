package com.pms.publicationmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class LoginRequestDto {
    @Email(message = "must be a valid email address")
    public String email;

    @NotEmpty(message = "must not be null or empty")
    public String password;

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public LoginRequestDto() {

    }
}
