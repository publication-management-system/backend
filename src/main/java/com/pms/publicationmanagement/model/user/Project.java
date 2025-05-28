package com.pms.publicationmanagement.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    private Set<User> users;

    @ManyToOne(fetch = FetchType.LAZY)
    private User projectOwner;
}
