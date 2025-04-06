package com.pms.publicationmanagement.dto;


import com.pms.publicationmanagement.model.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    public String firstName;

    public String middleName;

    public String lastName;

    public String email;

    public UUID id;

    public UserRole userRole;

    public String imageUrl;
}
