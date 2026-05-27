package com.pms.publicationmanagement.repository.projections;

public interface CitationsByYearProjection {
    String getYear();
    Long getCitationsCount();
}
