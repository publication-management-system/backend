package com.pms.publicationmanagement.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Invitation {
    @Id
    private UUID id;

    private String link;

    private String email;

    private String institutionId;

    private LocalDateTime createdAt;

    private LocalDateTime acceptedAt;
}
