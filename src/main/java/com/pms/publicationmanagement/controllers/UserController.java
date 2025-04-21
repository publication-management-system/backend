package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.AcceptInvitationDto;
import com.pms.publicationmanagement.dto.AddUserDto;
import com.pms.publicationmanagement.dto.UpdatePasswordDto;
import com.pms.publicationmanagement.dto.UserDto;
import com.pms.publicationmanagement.dto.user.UpdateNameDto;
import com.pms.publicationmanagement.mapper.UserDtoMapper;
import com.pms.publicationmanagement.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void addUser(@RequestBody AddUserDto addUserDto) {
        userService.addUser(addUserDto.firstName, addUserDto.middleName, addUserDto.lastName, addUserDto.email,
                addUserDto.password, addUserDto.userRole);
    }

    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void removeUserById(@PathVariable UUID id) { userService.removeUser(id); }

    @GetMapping("/by-name")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public UserDto searchUserByName(@RequestParam String firstName, @RequestParam String middleName, @RequestParam String lastName) {
        return UserDtoMapper.toUserDto(userService.searchUserByName(firstName, middleName, lastName));
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public UserDto getUserById(@PathVariable UUID id) { return UserDtoMapper.toUserDto(userService.findById(id)); }

    @PostMapping("/invite-user")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public UserDto acceptInvitation(@RequestBody AcceptInvitationDto acceptInvitationDto) {
        return UserDtoMapper.toUserDto(userService.acceptInvitation(acceptInvitationDto));
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<UserDto> findAllUsers() {
        return UserDtoMapper.toUserDtoList(userService.findAllUsers());
    }

    @PatchMapping("/{id}/name")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public UserDto updateUserName(@PathVariable UUID id, @RequestBody UpdateNameDto updateNameDto) {
        return userService.updateUserNameById(id, updateNameDto);
    }

    @PatchMapping("/{id}/password")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public UserDto updateUserName(@PathVariable UUID id, @RequestBody UpdatePasswordDto updateNameDto) {
        return userService.updatePasswordById(id, updateNameDto);
    }

    @PostMapping(value = "/{id}/profile-image", consumes = "multipart/form-data")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public UserDto uploadProfileImage(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        return userService.updateProfileImage(id, file);
    }

    @GetMapping("/institution/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<UserDto> findUsersFromInstitution(@PathVariable UUID id) {
        return UserDtoMapper.toUserDtoList(userService.findUsersFromInstitution(id));
    }
}
