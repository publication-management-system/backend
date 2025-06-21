package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.AddInvitationDto;
import com.pms.publicationmanagement.dto.InvitationDto;
import com.pms.publicationmanagement.mapper.InvitationDtoMapper;
import com.pms.publicationmanagement.service.user.InvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {
    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void addInvitation(@RequestBody AddInvitationDto addInvitationDto) {
        invitationService.sendInvitation(addInvitationDto);
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public InvitationDto findById(@RequestParam UUID id) {
        return InvitationDtoMapper.toInvitationDto(invitationService.findById(id));
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<InvitationDto> findAll() {
        return InvitationDtoMapper.toInvitationDtoList(invitationService.findAll());
    }

    @DeleteMapping
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void deleteInvitation(@RequestParam UUID id) {
        invitationService.deleteInvitation(id);
    }
}
