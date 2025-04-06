package com.pms.publicationmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class AddInvitationDto {

    public UUID id;

    public String link;

    public Boolean wasTaken;
}
