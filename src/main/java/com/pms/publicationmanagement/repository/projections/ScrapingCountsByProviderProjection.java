package com.pms.publicationmanagement.repository.projections;

public interface ScrapingCountsByProviderProjection {
    String getProvider();
    Long getCount();
}
