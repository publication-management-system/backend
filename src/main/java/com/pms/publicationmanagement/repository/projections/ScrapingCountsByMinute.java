package com.pms.publicationmanagement.repository.projections;

import java.time.LocalDateTime;

public interface ScrapingCountsByMinute {
    LocalDateTime getMinute();
    Long getCount();
}
