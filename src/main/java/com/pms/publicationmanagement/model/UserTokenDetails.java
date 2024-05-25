package com.pms.publicationmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserTokenDetails {
    private Long userId;
    private Long institutionId;
    private String email;
    private UserType role;
}
