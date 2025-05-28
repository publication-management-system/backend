package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.projects.CreateProjectDto;
import com.pms.publicationmanagement.dto.projects.ProjectDto;
import com.pms.publicationmanagement.mapper.ProjectDtoMapper;
import com.pms.publicationmanagement.service.user.ProjectService;
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
    public void addProject(@RequestBody CreateProjectDto projectDto, @RequestParam UUID userId) {
        projectService.addProject(projectDto, userId);
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void getProjectById(@PathVariable UUID id) {
        projectService.findById(id);
    }

    @GetMapping("/user/{userId}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<ProjectDto> getProjects(@PathVariable UUID userId) {
        return ProjectDtoMapper.toProjectDtoList(projectService.findAllByUserId(userId));
    }

    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
    }

    @PatchMapping("/{id}/title")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public ProjectDto updateProjectTitle(@PathVariable UUID id, @RequestBody ProjectDto projectDto) {
        return projectService.updateProjectTitleById(id, projectDto);
    }

    @PatchMapping("/{id}/description")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public ProjectDto updateTaskDescription(@PathVariable UUID id, @RequestBody ProjectDto projectDto) {
        return projectService.updateProjectDescriptionById(id, projectDto);
    }
}
