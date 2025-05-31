package com.pms.publicationmanagement.model.user;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="institution_id")
    private Institution institution;

    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String imageUrl;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "user_projects",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects = new ArrayList<>();

    public String getName() {
        if (middleName != null) {
            return String.format("%s %s %s", firstName, middleName, lastName);
        } else {
            return String.format("%s %s", firstName, lastName);
        }
    }
}
