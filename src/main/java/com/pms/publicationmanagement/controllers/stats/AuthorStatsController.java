package com.pms.publicationmanagement.controllers.stats;

import com.pms.publicationmanagement.dto.stats.AuthorStatistics;
import com.pms.publicationmanagement.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/public/stats")
public class AuthorStatsController {

    private final StatisticsService statisticsService;

    @GetMapping("/authors")
    public AuthorStatistics getInstitutionStats(@RequestParam(required = true) UUID authorId) {
        return statisticsService.getStatisticsByAuthorId(authorId);
    }

}
