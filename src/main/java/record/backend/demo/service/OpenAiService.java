package record.backend.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/";
    private static final String API_KEY = "your-openai-api-key";

    public String convertAudioToText(String audioFilePath) {
        // TODO: OpenAI Whisper API 또는 다른 음성 인식 API 호출 구현
        return "Converted text from audio";
    }

    public String extractSummary(String text) {
        // GPT API를 사용해 텍스트 요약
        RestTemplate restTemplate = new RestTemplate();
        // TODO: OpenAI GPT 호출 로직 추가
        return "Summary of the text";
    }
}
