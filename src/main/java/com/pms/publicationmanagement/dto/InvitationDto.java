package com.pms.publicationmanagement.dto;

public class InvitationDto {

    public Integer id;

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
