package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.session.LoginRequestDto;
import com.pms.publicationmanagement.dto.session.LoginResponseDto;
import com.pms.publicationmanagement.dto.session.RegisterRequestDto;
import com.pms.publicationmanagement.dto.session.RegisterResponseDto;
import com.pms.publicationmanagement.service.profiling.SessionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequest) {
        String accessToken = sessionService.loginUser(loginRequest.email, loginRequest.password);

        return new LoginResponseDto(accessToken);
    }

    @PostMapping("/register")
    public RegisterResponseDto register(@RequestBody RegisterRequestDto registerRequest) {

        return sessionService.registerUser(registerRequest);
    }

}
