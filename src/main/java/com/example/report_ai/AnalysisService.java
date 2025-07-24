package com.example.report_ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AnalysisService {

    private final GeminiClient geminiClient;
    private final ReportRepository reportRepository;

    public void analyzeAndSave(String input) throws JsonProcessingException {
        String prompt = """
            다음 입력값을 바탕으로
            1. 이상탐지 결과
            2. 예측 정보
            3. 원인 추정
            을 구분해서 JSON 형태로 응답해주세요.
            배열로 안나누고 그냥 붙여서 문장 나열해주세요
            입력값: %s
        """.formatted(input);

        String json = geminiClient.askGemini(prompt);
        System.out.println("Gemini 응답 : " + json);
        // JSON 파싱 → 각각 Report 엔티티에 저장
        Report report = parseAndSave(json);
        reportRepository.save(report);
    }

    private Report parseAndSave(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (json.startsWith("```")) {
            json = json.substring(json.indexOf('\n') + 1, json.lastIndexOf("```")).trim();
        }

        JsonNode node = mapper.readTree(json);

        Report report = new Report();
        report.setAnomaly(node.get("이상탐지 결과").asText());
        report.setPrediction(node.get("예측 정보").asText());
        report.setCause(node.get("원인 추정").asText());
        return report;
    }
}