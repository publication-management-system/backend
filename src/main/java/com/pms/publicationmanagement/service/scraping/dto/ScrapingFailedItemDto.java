package com.pms.publicationmanagement.service.scraping.dto;

import com.pms.publicationmanagement.model.scraping.enums.DataSourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScrapingFailedItemDto {
    public String id;
    public String type;
    public DataSourceType dataSource;
    public String details;
}
