package com.pms.publicationmanagement.controllers.publicendpoints;

import com.pms.publicationmanagement.dto.AcceptInvitationDto;
import com.pms.publicationmanagement.dto.InvitationDto;
import com.pms.publicationmanagement.dto.UserDto;
import com.pms.publicationmanagement.mapper.InvitationDtoMapper;
import com.pms.publicationmanagement.mapper.UserDtoMapper;
import com.pms.publicationmanagement.service.user.InvitationService;
import com.pms.publicationmanagement.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/public/invitations")
public class PublicInvitationController {

    private final InvitationService invitationService;
    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public InvitationDto findById(@PathVariable UUID id) {
        return InvitationDtoMapper.toInvitationDto(invitationService.findById(id));
    }


    @PostMapping("/accept-invitation")
    public UserDto acceptInvitation(@RequestBody AcceptInvitationDto acceptInvitationDto) {
        return UserDtoMapper.toUserDto(userService.acceptInvitation(acceptInvitationDto));
    }
}
