package com.pms.publicationmanagement.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class InvitationDto {
    @NotNull
    public UUID id;

    @NotNull
    public String link;

    public Boolean wasTaken;

    public InvitationDto(UUID id, String link, Boolean wasTaken) {
        this.id = id;
        this.link = link;
        this.wasTaken = wasTaken;
    }

    public InvitationDto() {
    }
}
