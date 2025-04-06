package com.pms.publicationmanagement.model.user;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

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

    public String getName() {
        if (middleName != null) {
            return String.format("%s %s %s", firstName, middleName, lastName);
        } else {
            return String.format("%s %s", firstName, lastName);
        }
    }
}
