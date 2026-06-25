package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.projects.*;
import com.pms.publicationmanagement.mapper.TaskDtoMapper;
import com.pms.publicationmanagement.model.projects.TaskState;
import com.pms.publicationmanagement.service.projects.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/{projectId}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public TaskDto addTask(@PathVariable UUID projectId, @RequestBody CreateTaskDto taskDto) {
        return taskService.addTask(projectId, taskDto.getTitle(), taskDto.getDescription());
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public TaskDto getTask(@PathVariable UUID id) {
        return TaskDtoMapper.toTaskDto(taskService.findById(id));
    }

    @GetMapping()
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<TaskDto> getTask() {
        return TaskDtoMapper.toTaskDtoList(taskService.findAll());
    }

    @GetMapping("/project/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<TaskDto> findTasksFromProject(@PathVariable UUID id) {
        return TaskDtoMapper.toTaskDtoList(taskService.findUsersFromInstitution(id));
    }

    @GetMapping("/with-resources/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public TaskWithResourcesDto getTaskWithResources(@PathVariable UUID id,
                                                     @RequestParam UUID projectId,
                                                     @RequestParam UUID userId) {
        return taskService.getTaskWithResourcesForUser(id, projectId, userId);
    }

    @PostMapping("/{id}/resources")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public TaskResourceDto saveTaskResource(@PathVariable UUID id,
                                            @RequestBody CreateTaskResourceDto taskResourceDto) {
        return taskService.saveTaskResource(id, taskResourceDto);
    }

    @PatchMapping("/{id}/resources/{resourceId}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public TaskResourceDto updateTaskResource(@PathVariable UUID id,
                                              @PathVariable UUID resourceId,
                                              @RequestBody UpdateTaskResourceDto taskResourceDto) {
        return taskService.updateTaskResource(id, resourceId, taskResourceDto);
    }

    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
    }

    @PatchMapping("/{id}/title")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public TaskDto updateTaskTitle(@PathVariable UUID id, @RequestBody TaskDto taskDto) {
        return taskService.updateTaskTitleById(id, taskDto);
    }

    @PatchMapping("/{id}/description")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public TaskDto updateTaskDescription(@PathVariable UUID id, @RequestBody TaskDto taskDto) {
        return taskService.updateTaskDescriptionById(id, taskDto);
    }

    @PatchMapping("/{id}/state")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public TaskDto updateTaskState(@PathVariable UUID id, @RequestParam TaskState taskState) {
        return taskService.updateTaskStateById(id, taskState);
    }
}
