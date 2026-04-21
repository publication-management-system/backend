package com.pms.publicationmanagement.dto.scraping;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnqueueScrapingRequestDto {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;

    @NotEmpty
    private UUID institutionId;

    @NotEmpty
    private UUID userId;

    @NotEmpty
    private String userName;
}
