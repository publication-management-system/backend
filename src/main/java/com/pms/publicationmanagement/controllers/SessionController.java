package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.AcceptInvitationDto;
import com.pms.publicationmanagement.dto.UserDto;
import com.pms.publicationmanagement.dto.session.LoginRequestDto;
import com.pms.publicationmanagement.dto.session.LoginResponseDto;
import com.pms.publicationmanagement.dto.session.RegisterRequestDto;
import com.pms.publicationmanagement.dto.session.RegisterResponseDto;
import com.pms.publicationmanagement.mapper.UserDtoMapper;
import com.pms.publicationmanagement.service.profiling.SessionService;
import com.pms.publicationmanagement.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    private final UserService userService;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequest) {
        String accessToken = sessionService.loginUser(loginRequest.email, loginRequest.password);

        return new LoginResponseDto(accessToken);
    }

    @PostMapping("/register")
    public RegisterResponseDto register(@RequestBody RegisterRequestDto registerRequest) {

        return sessionService.registerUser(registerRequest);
    }

    @PostMapping("/accept-invitation")
    public UserDto acceptInvitation(@RequestBody AcceptInvitationDto acceptInvitationDto) {
        return UserDtoMapper.toUserDto(userService.acceptInvitation(acceptInvitationDto));
    }
}
