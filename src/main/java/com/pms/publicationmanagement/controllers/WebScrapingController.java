package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.ScrapingRequestDto;
import com.pms.publicationmanagement.dto.ScrapingSessionDto;
import com.pms.publicationmanagement.service.scraping.WebScrapingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scraping-sessions")
@RequiredArgsConstructor
@Slf4j
public class WebScrapingController {

    private final WebScrapingService webScrapingService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public UUID scrapeRequest(@RequestBody ScrapingRequestDto requestDto) {
        log.info("Started scraping request {}", requestDto);
        return webScrapingService.runScraping(requestDto);
    }

    @GetMapping("/institution/{institutionId}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<ScrapingSessionDto> getSessions(@PathVariable String institutionId) {
        return webScrapingService.findAll(institutionId);
    }

    @GetMapping("/{sessionId}")
    public ScrapingSessionDto getSession(@PathVariable UUID sessionId) {
        return webScrapingService.findById(sessionId);
    }
}