package com.pms.publicationmanagement.service.user;

import com.pms.publicationmanagement.dto.projects.CreateProjectDto;
import com.pms.publicationmanagement.dto.projects.ProjectDto;
import com.pms.publicationmanagement.mapper.ProjectDtoMapper;
import com.pms.publicationmanagement.model.user.Project;
import com.pms.publicationmanagement.model.user.User;
import com.pms.publicationmanagement.repository.ProjectRepository;
import com.pms.publicationmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional
    public void addProject(CreateProjectDto createProjectDto, UUID userId) {
        User user = userService.findById(userId);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Project newProject = new Project();

        newProject.setId(UUID.randomUUID());
        newProject.setTitle(createProjectDto.getTitle());
        newProject.setDescription(createProjectDto.getDescription());
        newProject.setProjectOwner(user);
        Set<User> projectUsers = new HashSet<>();
        projectUsers.add(user);
        newProject.setUsers(projectUsers);

        Project saved = projectRepository.save(newProject);
        user.getProjects().add(saved);
        userRepository.save(user);
    }

    public List<Project> findAllByUserId(UUID userId) {
        return projectRepository.findAllByProjectOwnerId(userId);
    }

    public Project findById(UUID id) {
        Project project = projectRepository.findById(id).orElse(null);

        if(project == null) {
            throw new RuntimeException("User with id not found");
        }
        return project;

    }

    public void deleteProject(UUID id) { projectRepository.deleteById(id); }

    public ProjectDto updateProjectTitleById(UUID id, ProjectDto projectDto) {
        Project project = projectRepository.findById(id).orElse(null);

        if (project == null) {
            throw new RuntimeException("Task with id not found");
        }

        project.setTitle(projectDto.getTitle());

        projectRepository.save(project);
        return ProjectDtoMapper.toProjectDto(project);
    }

    public ProjectDto updateProjectDescriptionById(UUID id, ProjectDto projectDto) {
        Project project = projectRepository.findById(id).orElse(null);

        if (project == null) {
            throw new RuntimeException("Task with id not found");
        }

        project.setDescription(projectDto.getDescription());

        projectRepository.save(project);
        return ProjectDtoMapper.toProjectDto(project);
    }

}
