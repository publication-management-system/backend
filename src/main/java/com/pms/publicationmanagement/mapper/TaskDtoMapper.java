package com.pms.publicationmanagement.mapper;

import com.pms.publicationmanagement.dto.projects.TaskDto;
import com.pms.publicationmanagement.model.user.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDtoMapper {

    public static TaskDto toTaskDto(Task task){
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());

        return dto;
    }

    public static List<TaskDto> toTaskDtoList(List <Task> taskList) {
        List<TaskDto> result = new ArrayList<>();

        for(Task t : taskList){
            result.add(toTaskDto(t));
        }
        return result;
    }
}
