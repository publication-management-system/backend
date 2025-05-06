package com.pms.publicationmanagement.service.user;

import com.pms.publicationmanagement.dto.user.ProjectDto;
import com.pms.publicationmanagement.mapper.ProjectDtoMapper;
import com.pms.publicationmanagement.model.user.Project;
import com.pms.publicationmanagement.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public void AddProject(String title, String description) {
        Project newProject = new Project();

        newProject.setId(UUID.randomUUID());
        newProject.setTitle(title);
        newProject.setDescription(description);

        projectRepository.save(newProject);
    }

    public List<Project> findAll() { return projectRepository.findAll(); }

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

        project.setTitle(projectDto.title);

        projectRepository.save(project);
        return ProjectDtoMapper.toProjectDto(project);
    }

    public ProjectDto updateProjectDescriptionById(UUID id, ProjectDto projectDto) {
        Project project = projectRepository.findById(id).orElse(null);

        if (project == null) {
            throw new RuntimeException("Task with id not found");
        }

        project.setDescription(projectDto.description);

        projectRepository.save(project);
        return ProjectDtoMapper.toProjectDto(project);
    }

}
