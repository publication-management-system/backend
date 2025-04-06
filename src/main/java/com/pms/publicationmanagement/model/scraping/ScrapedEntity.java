package com.pms.publicationmanagement.model.scraping;

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
public class ScrapedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID parentId;

    @Enumerated(EnumType.STRING)
    private ScrapedEntityType type;

    @Enumerated(EnumType.STRING)
    private DataSourceType dataSource;

    @Column(columnDefinition="BLOB NOT NULL")
    private String payload;

    private UUID sessionId;
}