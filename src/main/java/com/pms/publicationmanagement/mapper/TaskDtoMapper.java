package com.pms.publicationmanagement.mapper;

import com.pms.publicationmanagement.dto.projects.TaskDto;
import com.pms.publicationmanagement.dto.projects.TaskResourceDto;
import com.pms.publicationmanagement.dto.projects.TaskWithResourcesDto;
import com.pms.publicationmanagement.model.projects.Task;
import com.pms.publicationmanagement.model.projects.TaskResource;

import java.util.ArrayList;
import java.util.List;

public class TaskDtoMapper {

    public static TaskDto toTaskDto(Task task){
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setState(task.getState());
        dto.setProjectId(task.getProject().getId());

        return dto;
    }

    public static List<TaskDto> toTaskDtoList(List <Task> taskList) {
        List<TaskDto> result = new ArrayList<>();

        for(Task t : taskList){
            result.add(toTaskDto(t));
        }
        return result;
    }

    public static TaskResourceDto toTaskResourceDto(TaskResource resource){
        TaskResourceDto dto = new TaskResourceDto();
        dto.setId(resource.getId());
        dto.setResourceName(resource.getResourceName());
        dto.setResourceType(resource.getResourceType());
        dto.setData(resource.getData());
        dto.setUrl(resource.getUrl());
        return dto;
    }

    public static TaskWithResourcesDto toTaskWithResourcesDto(Task task){
        TaskWithResourcesDto dto = new TaskWithResourcesDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setState(task.getState());
        dto.setProjectId(task.getProject().getId());

        List<TaskResourceDto> resources = task.getTaskResources() == null
                ? List.of()
                : task.getTaskResources().stream()
                  .map(TaskDtoMapper::toTaskResourceDto)
                  .toList();
        dto.setTaskResources(resources);

        return dto;
    }
}
