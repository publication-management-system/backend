package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.AddUserDto;
import com.pms.publicationmanagement.dto.UserDto;
import com.pms.publicationmanagement.mapper.UserDtoMapper;
import com.pms.publicationmanagement.services.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping
    public void addUser(@RequestBody AddUserDto addUserDto) {
        userService.addUser(addUserDto.firstName, addUserDto.middleName, addUserDto.lastName, addUserDto.email,
                addUserDto.password, addUserDto.id, addUserDto.userType);
    }

    @DeleteMapping("/{id}")
    public void removeUserById(@PathVariable Integer id) { userService.removeUser(id); }

    @GetMapping("/by-name")
    public UserDto searchUserByName(@RequestParam String firstName, @RequestParam String middleName, @RequestParam String lastName) {
        return UserDtoMapper.toUserDto(userService.searchUserByName(firstName, middleName, lastName));
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Integer id) { return UserDtoMapper.toUserDto(userService.findById(id)); }

    @GetMapping
    public List<UserDto> findAllUsers() {
        return UserDtoMapper.toUserDtoList(userService.findAllUsers());
    }

}
