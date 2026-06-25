package com.pms.publicationmanagement.service.projects;

import com.pms.publicationmanagement.dto.AddUserToProjectDto;
import com.pms.publicationmanagement.dto.UserDto;
import com.pms.publicationmanagement.dto.projects.CreateProjectDto;
import com.pms.publicationmanagement.dto.projects.ProjectDto;
import com.pms.publicationmanagement.dto.projects.UpdateTitleDescriptionDto;
import com.pms.publicationmanagement.mapper.ProjectDtoMapper;
import com.pms.publicationmanagement.mapper.UserDtoMapper;
import com.pms.publicationmanagement.model.projects.Project;
import com.pms.publicationmanagement.model.user.User;
import com.pms.publicationmanagement.repository.ProjectRepository;
import com.pms.publicationmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProjectDto addProject(CreateProjectDto createProjectDto, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project newProject = new Project();
        newProject.setId(UUID.randomUUID());
        newProject.setTitle(createProjectDto.getTitle());
        newProject.setDescription(createProjectDto.getDescription());
        newProject.setProjectOwnerId(user.getId());
        newProject.setCreatedAt(LocalDateTime.now());
        user.getProjects().add(newProject);
        userRepository.save(user);

        return ProjectDtoMapper.toProjectDto(newProject);
    }

    public List<ProjectDto> findAllByUserId(UUID userId) {
        var results = projectRepository.findProjectsByUserId(userId);

        return ProjectDtoMapper.toProjectDtoList(results);
    }

    public ProjectDto findById(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return ProjectDtoMapper.toProjectDto(project);
    }

    public void deleteProject(UUID id) { projectRepository.deleteById(id); }

    public ProjectDto updateTitleAndDescription(UUID id, UpdateTitleDescriptionDto projectDto) {
        Project project = projectRepository.findById(id).orElse(null);

        if (project == null) {
            throw new RuntimeException("Task with id not found");
        }

        project.setDescription(projectDto.getDescription());
        project.setTitle(projectDto.getTitle());

        projectRepository.save(project);
        return ProjectDtoMapper.toProjectDto(project);
    }

    @Transactional
    public UserDto addUserToProject(AddUserToProjectDto addUserToProjectDto) {
        User user = userRepository.findByEmail(addUserToProjectDto.getEmail());

        if (user == null) {
            throw new RuntimeException("User with email not found");
        }

        Project project = projectRepository.findById(addUserToProjectDto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project with id not found"));

        user.addProject(project);

        userRepository.save(user);

        return UserDtoMapper.toUserDto(user);
    }
}
