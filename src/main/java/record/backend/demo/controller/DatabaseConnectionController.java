package record.backend.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import record.backend.demo.service.DatabaseConnectionService;

@RestController
@RequestMapping("/health")
public class DatabaseConnectionController {

    @Autowired
    private DatabaseConnectionService databaseConnectionService;

    /**
     * 데이터베이스 연결 상태를 확인하는 API
     * @return 데이터베이스 연결 상태 (200 OK 또는 500 Internal Server Error)
     */
    @GetMapping("/db")
    public ResponseEntity<String> checkDatabaseConnection() {
        boolean isConnected = databaseConnectionService.isDatabaseConnected();

        if (isConnected) {
            return ResponseEntity.ok("Database is connected.");
        } else {
            return ResponseEntity.status(500).body("Database connection failed.");
        }
    }
}