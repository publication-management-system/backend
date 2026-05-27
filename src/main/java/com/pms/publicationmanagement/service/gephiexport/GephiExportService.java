package com.pms.publicationmanagement.service.gephiexport;

import com.pms.publicationmanagement.model.profiling.Author;
import com.pms.publicationmanagement.model.profiling.Citation;
import com.pms.publicationmanagement.model.profiling.Document;
import com.pms.publicationmanagement.repository.AuthorRepository;
import com.pms.publicationmanagement.repository.CitationRepository;
import com.pms.publicationmanagement.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.plugin.ExporterGEXF;
import org.gephi.project.api.ProjectController;
import org.openide.util.Lookup;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.StringWriter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GephiExportService {

    private final AuthorRepository authorRepository;
    private final CitationRepository citationRepository;


    public String getVisualizeGraph() {
        var authors = authorRepository.findAllWithDocuments();

        var projectController = Lookup.getDefault().lookup(ProjectController.class);
        projectController.newProject();
        var workspace = projectController.getCurrentWorkspace();

        var graphModel = Lookup.getDefault()
                .lookup(GraphController.class)
                .getGraphModel();

        var nodeTable = graphModel.getNodeTable();
        nodeTable.addColumn("nodeType",    String.class);
        nodeTable.addColumn("institution", String.class);
        nodeTable.addColumn("role",        String.class);
        nodeTable.addColumn("topics",      String.class);
        nodeTable.addColumn("imageUrl",    String.class);
        nodeTable.addColumn("year",        String.class);
        nodeTable.addColumn("publisher",   String.class);
        nodeTable.addColumn("link",        String.class);
        nodeTable.addColumn("refId",       String.class);

        var edgeTable = graphModel.getEdgeTable();
        edgeTable.addColumn("relationship", String.class);

        var graph = graphModel.getDirectedGraph();
        var factory = graphModel.factory();

        Map<UUID, Node> nodeIndex = new HashMap<>();
        Set<String> edgeKeys = new HashSet<>();

        for (Author author : authors) {
            Node authorNode = nodeIndex.computeIfAbsent(author.getId(), id -> {
                Node n = factory.newNode(id.toString());
                n.setLabel(safe(author.getName(),
                        (safe(author.getFirstName(), "") + " " + safe(author.getLastName(), "")).trim()));
                n.setAttribute("nodeType", "AUTHOR");
                n.setAttribute("institution", author.getInstitution());
                n.setAttribute("role", author.getInstitutionRole());
                n.setAttribute("topics", author.getTopics());
                n.setAttribute("imageUrl", author.getImageUrl());
                n.setAttribute("refId", author.getInternalRefId());
                n.setColor(new Color(0x1f78b4));
                n.setSize(20f);
                graph.addNode(n);
                return n;
            });

            if (author.getDocuments() == null) continue;

            for (Document doc : author.getDocuments()) {
                Node docNode = nodeIndex.computeIfAbsent(doc.getId(), id -> {
                    Node n = factory.newNode(id.toString());
                    n.setLabel(safe(doc.getTitle(), "Untitled"));
                    n.setAttribute("nodeType", "DOCUMENT");
                    n.setAttribute("year", doc.getPublicationDate());
                    n.setAttribute("publisher", doc.getPublisher());
                    n.setAttribute("link", doc.getLink());
                    n.setAttribute("refId", doc.getInternalRefId());
                    n.setColor(new Color(0x33a02c));
                    n.setSize(15f);
                    graph.addNode(n);
                    return n;
                });

                String authoredKey = authorNode.getId() + "->" + docNode.getId();
                if (edgeKeys.add(authoredKey)) {
                    Edge authored = factory.newEdge(authorNode, docNode, true);
                    authored.setAttribute("relationship", "authored");
                    graph.addEdge(authored);
                }

                var citations = citationRepository.findAllByDocumentId(doc.getId());
                for (Citation cit : citations) {
                    Node citNode = nodeIndex.computeIfAbsent(cit.getId(), id -> {
                        Node n = factory.newNode(id.toString());
                        n.setLabel(safe(cit.getTitle(), "Citation"));
                        n.setAttribute("nodeType", "CITATION");
                        n.setAttribute("link", cit.getLink());
                        n.setColor(new Color(0xe31a1c));
                        n.setSize(8f);
                        graph.addNode(n);
                        return n;
                    });

                    String citesKey = citNode.getId() + "->" + docNode.getId();
                    if (edgeKeys.add(citesKey)) {
                        Edge cites = factory.newEdge(citNode, docNode, true);
                        cites.setAttribute("relationship", "cites");
                        graph.addEdge(cites);
                    }
                }
            }
        }

        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        ExporterGEXF exporter = (ExporterGEXF) ec.getExporter("gexf");
        exporter.setWorkspace(workspace);
        exporter.setExportColors(true);
        exporter.setExportSize(true);
        exporter.setExportPosition(false);

        StringWriter sw = new StringWriter();
        ec.exportWriter(sw, exporter);
        return sw.toString();
    }

    private static String safe(String v, String fallback) {
        return (v == null || v.isBlank()) ? fallback : v;
    }
}
