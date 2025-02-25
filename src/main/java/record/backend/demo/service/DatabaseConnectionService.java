package record.backend.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseConnectionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 데이터베이스 연결 상태를 확인하는 메서드
     * @return true(연결 성공), false(연결 실패)
     */
    public boolean isDatabaseConnected() {
        try {
            // 간단한 쿼리 실행 (SELECT 1)
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return result != null && result == 1;
        } catch (Exception e) {
            // 예외 발생 시 연결 실패로 간주
            e.printStackTrace();
            return false;
        }
    }
}