package com.pms.publicationmanagement.dto.session;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDto {
    public UUID userId;

    public UUID institutionId;
}
