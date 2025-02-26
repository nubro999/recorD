package record.backend.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAiService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/";
    private static final String API_KEY = "your-openai-api-key";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OpenAiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * OpenAI Whisper API를 사용하여 음성 파일을 텍스트로 변환합니다.
     *
     * @param audioFilePath 음성 파일 경로
     * @return 변환된 텍스트
     * @throws IOException 파일 읽기 오류
     */
    public String convertAudioToText(String audioFilePath) throws IOException {
        String url = OPENAI_API_URL + "audio/transcriptions";

        // 오디오 파일 준비
        File audioFile = new File(audioFilePath);
        if (!audioFile.exists()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다: " + audioFilePath);
        }

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(API_KEY);

        // 요청 본문에 파일 추가
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(audioFile));
        body.add("model", "whisper-1"); // Whisper 모델 지정

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            return jsonResponse.get("text").asText();
        } else {
            throw new RuntimeException("OpenAI Whisper API 호출 실패: " + response.getBody());
        }
    }

    /**
     * OpenAI GPT API를 사용하여 텍스트 요약을 생성합니다.
     *
     * @param text 입력 텍스트
     * @return 요약된 텍스트
     */
    public String extractSummary(String text) {
        String url = OPENAI_API_URL + "chat/completions";

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        // 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", new Object[]{
                Map.of("role", "system", "content", "You are a helpful assistant."),
                Map.of("role", "user", "content", "다음 텍스트를 요약해 주세요: " + text)
        });
        requestBody.put("max_tokens", 100);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                return jsonResponse.get("choices").get(0).get("message").get("content").asText();
            } catch (Exception e) {
                throw new RuntimeException("응답 파싱 실패: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("OpenAI GPT API 호출 실패: " + response.getBody());
        }
    }
}