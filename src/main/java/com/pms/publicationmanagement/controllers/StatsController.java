package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.stats.KeyValStatsDto;
import com.pms.publicationmanagement.service.stats.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/institution/{institutionId}")
    public List<KeyValStatsDto> getInstitutionStats(@PathVariable UUID institutionId) {
        return statsService.getInstitutionStats(institutionId);
    }

}
