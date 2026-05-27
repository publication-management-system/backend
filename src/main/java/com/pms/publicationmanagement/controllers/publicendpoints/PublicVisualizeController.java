package com.pms.publicationmanagement.controllers.publicendpoints;

import com.pms.publicationmanagement.service.gephiexport.GephiExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/visualize")
public class PublicVisualizeController {

    private final GephiExportService gephiExportService;

    @GetMapping
    public String visualize() {
        return gephiExportService.getVisualizeGraph();
    }
}
