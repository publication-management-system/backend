package com.pms.publicationmanagement.repository.projections;

public interface DocumentsByYearProjection {
    String getYear();
    Long getDocumentCount();
}
