package com.pms.publicationmanagement.dto;

public class AddInvitationDto {

    public Integer id;

    public String link;

    public Boolean wasTaken;

    public AddInvitationDto(Integer id, String link, Boolean wasTaken) {
        this.id = id;
        this.link = link;
        this.wasTaken = wasTaken;
    }

    public AddInvitationDto() {
    }
}
