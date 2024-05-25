package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.LoginRequestDto;
import com.pms.publicationmanagement.dto.LoginResponseDto;
import com.pms.publicationmanagement.dto.RegisterRequestDto;
import com.pms.publicationmanagement.dto.UserDto;
import com.pms.publicationmanagement.mapper.UserDtoMapper;
import com.pms.publicationmanagement.model.User;
import com.pms.publicationmanagement.services.InstitutionService;
import com.pms.publicationmanagement.services.SessionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {

    private final SessionService sessionService;

    private final InstitutionService institutionService;

    public SessionController(SessionService sessionService, InstitutionService institutionService) {
        this.sessionService = sessionService;
        this.institutionService = institutionService;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequest) {
        String accessToken = sessionService.loginUser(loginRequest.email, loginRequest.password);

        return new LoginResponseDto(accessToken);
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody RegisterRequestDto registerRequest) {

        User savedUser = sessionService.registerUser(registerRequest.firstName, registerRequest.middleName, registerRequest.lastName,
                registerRequest.email, registerRequest.password, registerRequest.userType,
                registerRequest.institutionName, registerRequest.address, registerRequest.phoneNumber, registerRequest.institutionEmail);



        return UserDtoMapper.toUserDto(savedUser);
    }

}
