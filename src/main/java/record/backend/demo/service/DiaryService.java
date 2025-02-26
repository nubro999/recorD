package record.backend.demo.service;
import record.backend.demo.entity.Diary;
import record.backend.demo.repository.DiaryRepository;
import record.backend.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DiaryService {

    @Autowired
    private DiaryRepository diaryRepository;

    @Autowired
    private OpenAiService openAiService;

    public Diary saveDiary(User user, LocalDate date, String audioFilePath) {
        // 1. 음성을 텍스트로 변환
        String text = openAiService.convertAudioToText(audioFilePath);

        // 2. 텍스트에서 핵심 내용 추출
        String summary = openAiService.extractSummary(text);

        // 3. 일기 저장
        Diary diary = new Diary();
        diary.setUser(user); // 사용자와 연결
        diary.setDate(date);
        diary.setContent(text);
        diary.setSummary(summary);

        return diaryRepository.save(diary);
    }

    public Optional<Diary> getDiaryByDate(User user, LocalDate date) {
        return diaryRepository.findByDate(date)
                .filter(diary -> diary.getUser().equals(user)); // 사용자 확인
    }
}