package com.pms.publicationmanagement.model.user;

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
@NoArgsConstructor
@AllArgsConstructor
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="institution", cascade = CascadeType.ALL)
    private List<User> users;

    private String name;

    private String address;

    private String phoneNumber;

    private String email;
}
