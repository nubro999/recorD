package record.backend.demo.controller;

import record.backend.demo.entity.Diary;
import record.backend.demo.entity.User;
import record.backend.demo.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import record.backend.demo.service.UserService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public Diary saveDiary(@RequestParam("email") String email,
                           @RequestParam("date") String date,
                           @RequestParam("audio") MultipartFile audioFile) throws IOException {
        // 사용자 확인
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 파일 저장
        File tempFile = File.createTempFile("audio", ".wav");
        audioFile.transferTo(tempFile);

        // 서비스 호출
        LocalDate diaryDate = LocalDate.parse(date);
        return diaryService.saveDiary(user, diaryDate, tempFile.getAbsolutePath());
    }

    @GetMapping("/{email}/{date}")
    public Optional<Diary> getDiary(@PathVariable String email, @PathVariable String date) {
        // 사용자 확인
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        LocalDate diaryDate = LocalDate.parse(date);
        return diaryService.getDiaryByDate(user, diaryDate);
    }
}