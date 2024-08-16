package kopis.k_backend.job;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 고유 식별자

    private String start;
    private String end;
    private String status;
    private String type; // 작업 유형


    // 기본 생성자
    public Job() {
    }

    // 생성자
    public Job(String start, String end, String status, String type) {
        this.start = start;
        this.end = end;
        this.status = status;
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setEnd(String end) {
        this.end = end;
    }
}
