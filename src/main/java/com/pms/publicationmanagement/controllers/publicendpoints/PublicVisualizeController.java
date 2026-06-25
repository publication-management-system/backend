package com.pms.publicationmanagement.controllers.publicendpoints;

import com.pms.publicationmanagement.service.gephiexport.GephiExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/visualize")
public class PublicVisualizeController {

    private final GephiExportService gephiExportService;

    @GetMapping
    public String visualize() {
        return gephiExportService.getVisualizeGraph();
    }

    @GetMapping(value = "/download", produces = "application/xml")
    public ResponseEntity<byte[]> downloadGexf() {
        String gexf = gephiExportService.getVisualizeGraph();

        byte[] content = gexf.getBytes(StandardCharsets.UTF_8);

        String fileName = String.format(
                "export_visualize_%s.gexf",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_XML)
                .contentLength(content.length)
                .body(content);
    }
}
