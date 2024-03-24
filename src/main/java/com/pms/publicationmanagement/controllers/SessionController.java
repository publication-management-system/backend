package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.LoginRequestDto;
import com.pms.publicationmanagement.dto.RegisterRequestDto;
import com.pms.publicationmanagement.dto.UserDto;
import com.pms.publicationmanagement.mapper.UserDtoMapper;
import com.pms.publicationmanagement.model.Institution;
import com.pms.publicationmanagement.model.User;
import com.pms.publicationmanagement.services.InstitutionService;
import com.pms.publicationmanagement.services.SessionService;
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
    public UserDto login(@RequestBody LoginRequestDto loginRequest) {
        User loggedIn = sessionService.loginUser(loginRequest.email, loginRequest.password);

        return UserDtoMapper.toUserDto(loggedIn);
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody RegisterRequestDto registerRequest) {

//        Institution savedInstitution = institutionService.addInstitution(registerRequest.institutionName, registerRequest.address,
//                registerRequest.phoneNumber, registerRequest.institutionEmail);

        User savedUser = sessionService.registerUser(registerRequest.firstName, registerRequest.middleName, registerRequest.lastName,
                registerRequest.email, registerRequest.password, registerRequest.id, registerRequest.userType,
                registerRequest.institutionName, registerRequest.address, registerRequest.phoneNumber, registerRequest.institutionEmail);

//        savedInstitution.getUsers().add(savedUser);


        return UserDtoMapper.toUserDto(savedUser);
    }

}
