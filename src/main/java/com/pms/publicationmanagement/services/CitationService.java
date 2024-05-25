package com.pms.publicationmanagement.services;

import com.pms.publicationmanagement.model.Citation;
import com.pms.publicationmanagement.model.Document;
import com.pms.publicationmanagement.repository.SpringJpaCitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitationService {

    private final SpringJpaCitationRepository citationRepository;

    public void addCitation(Integer id, String title, String link, Document document) {
        citationRepository.save(new Citation(id, title, link, document));
    }

    public void deleteCitation(Integer id) {
        citationRepository.deleteById(id);
    }

    public List<Citation> findAll() {
        return citationRepository.findAll();
    }
}
