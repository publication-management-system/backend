package com.pms.publicationmanagement.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNameDto {
    private String firstName;

    private String lastName;

    private String middleName;
}
