package com.example.report_ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final AnalysisService analysisService;
    private final ReportRepository reportRepository;

    @PostMapping("/analyze")
    public ResponseEntity<Void> analyze(@RequestBody String body) throws JsonProcessingException {
        analysisService.analyzeAndSave(body);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reports")
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

}