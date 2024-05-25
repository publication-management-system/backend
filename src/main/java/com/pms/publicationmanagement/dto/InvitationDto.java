package com.pms.publicationmanagement.dto;

import jakarta.validation.constraints.NotNull;

public class InvitationDto {
    @NotNull
    public Integer id;

    @NotNull
    public String link;

    public Boolean wasTaken;

    public InvitationDto(Integer id, String link, Boolean wasTaken) {
        this.id = id;
        this.link = link;
        this.wasTaken = wasTaken;
    }

    public InvitationDto() {
    }
}
