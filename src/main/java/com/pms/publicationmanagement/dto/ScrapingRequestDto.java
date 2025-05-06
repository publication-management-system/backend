package com.pms.publicationmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrapingRequestDto {
    private String firstName;
    private String lastName;
    private String institutionName;
    private String userId;
    private String userName;
    private String institutionId;
}
