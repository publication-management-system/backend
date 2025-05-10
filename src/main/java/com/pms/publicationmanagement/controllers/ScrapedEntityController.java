package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.model.scraping.DataSourceType;
import com.pms.publicationmanagement.model.scraping.ScrapedEntity;
import com.pms.publicationmanagement.model.scraping.ScrapedEntityType;
import com.pms.publicationmanagement.service.scraping.ScrapedEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scrapedentity")
public class ScrapedEntityController {
    private final ScrapedEntityService scrapedEntityService;

    public ScrapedEntityController(ScrapedEntityService scrapedEntityService) {
        this.scrapedEntityService = scrapedEntityService;
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<ScrapedEntity> getScrapedEntity(
            @PathVariable UUID id,
            @RequestParam(value = "source", defaultValue = "GOOGLE_SCHOLAR, required = false") DataSourceType dataSource,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return scrapedEntityService.getEntitiesFromSessionIdWithSource(id, dataSource, pageNumber, pageSize);
    }
}
