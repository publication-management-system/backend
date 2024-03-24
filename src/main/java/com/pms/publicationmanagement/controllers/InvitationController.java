package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.AddInvitationDto;
import com.pms.publicationmanagement.dto.InvitationDto;
import com.pms.publicationmanagement.mapper.InvitationDtoMapper;
import com.pms.publicationmanagement.services.InvitationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping
    public void AddInvitation(@RequestBody AddInvitationDto addInvitationDto) {
        invitationService.addInvitattion(addInvitationDto.link);
    }

    @GetMapping("/available")
    public List<InvitationDto> findAvailableInvitations(@RequestParam Boolean wasTaken) {
        return InvitationDtoMapper.toInvitationDtoList(invitationService.findAvailable(wasTaken));
    }

    @GetMapping
    public List<InvitationDto> findAll() {
        return InvitationDtoMapper.toInvitationDtoList(invitationService.findAll());
    }

    @DeleteMapping
    public void deleteInvitation(@RequestParam Integer id) {
        invitationService.deleteInvitation(id);
    }


}
