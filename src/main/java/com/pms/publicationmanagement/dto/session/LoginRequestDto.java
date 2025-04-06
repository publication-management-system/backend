package com.pms.publicationmanagement.dto.session;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    @Email(message = "must be a valid email address")
    public String email;

    @NotEmpty(message = "must not be null or empty")
    public String password;
}
