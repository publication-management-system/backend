package com.pms.publicationmanagement.service.profiling;

import com.pms.publicationmanagement.model.profiling.Citation;
import com.pms.publicationmanagement.model.profiling.Document;
import com.pms.publicationmanagement.repository.CitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CitationService {

    private final CitationRepository citationRepository;

    public void addCitation(UUID id, String title, String link, Document document) {
        citationRepository.save(new Citation(id, title, link, document));
    }

    public void deleteCitation(UUID id) {
        citationRepository.deleteById(id);
    }

    public List<Citation> findAll() {
        return citationRepository.findAll();
    }
}
