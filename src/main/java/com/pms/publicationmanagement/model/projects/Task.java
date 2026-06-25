package com.pms.publicationmanagement.model.projects;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="project_id")
    private Project project;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskState state;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="task", cascade = CascadeType.ALL)
    private List<TaskResource> taskResources;

    public void addResource(TaskResource taskResource) {
        taskResources.add(taskResource);
        taskResource.setTask(this);
    }
}
