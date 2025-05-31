package com.pms.publicationmanagement.service.projects;

import com.pms.publicationmanagement.dto.projects.CreateProjectDto;
import com.pms.publicationmanagement.dto.projects.ProjectDto;
import com.pms.publicationmanagement.dto.projects.UpdateTitleDescriptionDto;
import com.pms.publicationmanagement.mapper.ProjectDtoMapper;
import com.pms.publicationmanagement.model.user.Project;
import com.pms.publicationmanagement.model.user.User;
import com.pms.publicationmanagement.repository.ProjectRepository;
import com.pms.publicationmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public Page<Project> findAllByUserId(UUID userId, int pageNumber, int pageSize) {
        return projectRepository.findAllByProjectOwnerId(userId,
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    public Project findById(UUID id) {
        Project project = projectRepository.findById(id).orElse(null);

        if(project == null) {
            throw new RuntimeException("User with id not found");
        }
        return project;
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
}
