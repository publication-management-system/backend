package com.pms.publicationmanagement.model.projects;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskResource {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String resourceName;

    @Enumerated(EnumType.STRING)
    private TaskResourceType resourceType;

    private String data;

    private String url;

    @ManyToOne
    @JoinColumn(name="task_id")
    private Task task;
}
