package com.pms.publicationmanagement.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrapingStatsDto {
    private List<ScrapingItemByMinute> lastTenMinutes;
}
