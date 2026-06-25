package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.AddUserToProjectDto;
import com.pms.publicationmanagement.dto.UserDto;
import com.pms.publicationmanagement.dto.projects.CreateProjectDto;
import com.pms.publicationmanagement.dto.projects.ProjectDto;
import com.pms.publicationmanagement.dto.projects.UpdateTitleDescriptionDto;
import com.pms.publicationmanagement.service.projects.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public ProjectDto addProject(@RequestBody CreateProjectDto projectDto, @RequestParam UUID userId) {
        return projectService.addProject(projectDto, userId);
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public ProjectDto getProjectById(@PathVariable UUID id) {
        return projectService.findById(id);
    }

    @GetMapping("/user/{userId}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<ProjectDto> getProjects(@PathVariable UUID userId) {
        return projectService.findAllByUserId(userId);
    }

    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
    }

    @PatchMapping("/{id}/edit")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public ProjectDto updateTaskDescription(@PathVariable UUID id, @RequestBody UpdateTitleDescriptionDto updateTitleDescriptionDto) {
        return projectService.updateTitleAndDescription(id, updateTitleDescriptionDto);
    }

    @PostMapping("/add-user")
    public UserDto addUser(@RequestBody AddUserToProjectDto addUserToProjectDto)
    {
        return projectService.addUserToProject(addUserToProjectDto);
    }
}
