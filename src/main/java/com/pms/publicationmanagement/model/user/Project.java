package com.pms.publicationmanagement.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="project", cascade = CascadeType.ALL)
    private List<Task> task;

    private String title;

    private String description;

    @ManyToMany(mappedBy = "projects")
    private List<User> users = new ArrayList<>();

    private UUID projectOwnerId;

    private LocalDateTime createdAt;
}
