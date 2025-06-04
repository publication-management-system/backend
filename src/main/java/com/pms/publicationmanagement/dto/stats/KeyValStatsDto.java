package com.pms.publicationmanagement.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValStatsDto {
    private String key;

    private String value;
}
