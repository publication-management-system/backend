package com.pms.publicationmanagement.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrapingItemByMinute {
    private LocalDateTime minute;
    private Long count;
}
