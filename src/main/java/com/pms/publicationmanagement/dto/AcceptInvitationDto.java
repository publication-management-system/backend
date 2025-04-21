package com.pms.publicationmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AcceptInvitationDto {
    public String email;

    public UUID invitationId;

    public String firstName;

    public String middleName;

    public String lastName;

    public String password;

}
