package com.pms.publicationmanagement.service.statistics;

import com.pms.publicationmanagement.dto.stats.AuthorStatistics;
import com.pms.publicationmanagement.dto.stats.CitationsByYear;
import com.pms.publicationmanagement.dto.stats.DocumentByYear;
import com.pms.publicationmanagement.model.profiling.Citation;
import com.pms.publicationmanagement.repository.AuthorRepository;
import com.pms.publicationmanagement.repository.CitationRepository;
import com.pms.publicationmanagement.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final AuthorRepository authorRepository;
    private final DocumentRepository documentRepository;
    private final CitationRepository citationRepository;

    public AuthorStatistics getStatisticsByAuthorId(UUID authorId) {
        List<DocumentByYear> documentByYears = documentRepository.findDocStatsByYearAndAuthorId(authorId)
                .stream()
                .map(d -> new DocumentByYear(d.getYear(), d.getDocumentCount()))
                .toList();

        List<CitationsByYear> citationsByYears = citationRepository.getCitationsByYearForAuthor(authorId)
                .stream()
                .map(c -> new CitationsByYear(c.getYear(), c.getCitationsCount()))
                .toList();

        Long citationsCount = citationRepository.findTotalCitationCountByAuthorId(authorId);

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        List<String> topics = Optional.ofNullable(author.getTopics())
                .stream()
                .flatMap(s -> Arrays.stream(s.split(",")))
                .map(String::trim)
                .toList();


        return new AuthorStatistics(documentByYears, citationsByYears, citationsCount, topics);
    }
}
