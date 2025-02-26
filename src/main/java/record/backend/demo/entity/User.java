package record.backend.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; // 사용자 이메일 (고유값)

    @Column(nullable = false)
    private String name; // 사용자 이름

    @Column(nullable = false)
    private String password; // 비밀번호

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries; // 사용자의 일기 목록
}
