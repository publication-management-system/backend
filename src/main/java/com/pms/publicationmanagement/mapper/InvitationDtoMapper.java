package com.pms.publicationmanagement.mapper;

import com.pms.publicationmanagement.dto.InvitationDto;
import com.pms.publicationmanagement.model.user.Invitation;

import java.util.ArrayList;
import java.util.List;



public class InvitationDtoMapper {

    public static InvitationDto toInvitationDto(Invitation invitation) {

        InvitationDto invitationDto = new InvitationDto();
        invitationDto.id = invitation.getId();
        invitationDto.link = invitation.getLink();
        invitationDto.wasTaken = invitation.isWasTaken();
        return invitationDto;
    }

    public static List<InvitationDto> toInvitationDtoList(List<Invitation> invitationList) {
        List<InvitationDto> result = new ArrayList<>();

        for(Invitation i : invitationList) {
            result.add(toInvitationDto(i));
        }
        return result;
    }
}
