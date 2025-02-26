package record.backend.demo.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date; // 일기 날짜

    @Lob
    @Column(nullable = false)
    private String content; // 전체 텍스트

    @Lob
    private String summary; // 핵심 요약

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자와의 관계
}
