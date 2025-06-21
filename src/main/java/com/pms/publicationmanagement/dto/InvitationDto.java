package com.pms.publicationmanagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationDto {
    @NotNull
    public UUID id;
    @NotNull
    public String link;
    public String email;
    public String acceptedAt;
    public String createdAt;
}
