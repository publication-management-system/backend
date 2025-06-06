package com.pms.publicationmanagement.service.projects;

import com.pms.publicationmanagement.dto.projects.TaskDto;
import com.pms.publicationmanagement.mapper.TaskDtoMapper;
import com.pms.publicationmanagement.model.user.Project;
import com.pms.publicationmanagement.model.user.Task;
import com.pms.publicationmanagement.model.user.TaskState;
import com.pms.publicationmanagement.repository.ProjectRepository;
import com.pms.publicationmanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskDto addTask(UUID projectId, String title, String description) {
        Task newTask = new Task();

        Project project =  projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        newTask.setProject(project);
        newTask.setId(UUID.randomUUID());
        newTask.setTitle(title);
        newTask.setDescription(description);
        newTask.setState(TaskState.Backlog);

        taskRepository.save(newTask);

        return TaskDtoMapper.toTaskDto(newTask);
    }

    public List<Task> findAll() { return taskRepository.findAll(); }

    public Task findById(UUID id) {
        Task task = taskRepository.findById(id).orElse(null);

        if(task == null) {
            throw new RuntimeException("User with id not found");
        }
        return task;

    }

    public void deleteTask(UUID id) { taskRepository.deleteById(id); }

    public List<Task> findUsersFromInstitution(UUID projectId) {
        return taskRepository.findAllByProjectId(projectId);
    }

    public TaskDto updateTaskTitleById(UUID id, TaskDto taskDto) {
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            throw new RuntimeException("Task with id not found");
        }

        task.setTitle(taskDto.getTitle());

        taskRepository.save(task);
        return TaskDtoMapper.toTaskDto(task);

    }

    public TaskDto updateTaskDescriptionById(UUID id, TaskDto taskDto) {
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            throw new RuntimeException("Task with id not found");
        }

        task.setDescription(taskDto.getDescription());

        taskRepository.save(task);
        return TaskDtoMapper.toTaskDto(task);

    }

    public TaskDto updateTaskStateById(UUID id, TaskState state) {
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            throw new RuntimeException("Task with id not found");
        }

        task.setState(state);

        taskRepository.save(task);
        return TaskDtoMapper.toTaskDto(task);
    }
}
